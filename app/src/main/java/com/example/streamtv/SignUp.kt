package com.example.streamtv

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : Fragment() {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.sign_up, container, false)

        val signupButton = rootView.findViewById<Button>(R.id.signupButton)
        val loginLink = rootView.findViewById<TextView>(R.id.loginLink)

        val loginInput = rootView.findViewById<EditText>(R.id.loginInput)
        val emailInput = rootView.findViewById<EditText>(R.id.emailInput)
        val passwordInput = rootView.findViewById<EditText>(R.id.passwordInput)

        signupButton.setOnClickListener {
            navController = rootView.findNavController()
            if (isValid(loginInput, emailInput, passwordInput)) {
                createUser(getEditTextValue(loginInput), getEditTextValue(emailInput),
                    getEditTextValue(passwordInput))
            }
        }
        loginLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_signUp_to_login)
        }
        return rootView
    }

    private fun createUser(login: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user == null) {
                        Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.uid).setValue(User(login, email))
                        .addOnCompleteListener(requireActivity()) { dbtask ->
                            if (dbtask.isSuccessful) {
                                Log.d("SignUp", "createUserWithEmail:success")
                                navController.navigate(R.id.action_signUp_to_profile)
                            }
                        }
                } else {
                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun isValid(loginInput: EditText, emailInput: EditText, passwordInput: EditText): Boolean {
        val login = getEditTextValue(loginInput)
        val email = getEditTextValue(emailInput)
        val password = getEditTextValue(passwordInput)

        if (login.isEmpty()) {
            loginInput.error = "Login is required"
            loginInput.requestFocus()
            return false
        }
        if (email.isEmpty()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Provide valid email address"
            emailInput.requestFocus()
            return false
        }

        if (password.length < 6) {
            passwordInput.error = "Min password length is 6"
            passwordInput.requestFocus()
            return false
        }

        return true
    }

    private fun getEditTextValue(editText: EditText): String {
        return editText.text.toString().trim()
    }
}