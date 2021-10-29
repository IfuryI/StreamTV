package com.example.streamtv

import android.os.Bundle
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

class Login : Fragment() {
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
        val rootView = inflater.inflate(R.layout.login, container, false)

        val loginButton = rootView.findViewById<Button>(R.id.loginButton)
        val signupLink = rootView.findViewById<TextView>(R.id.signupLink)

        val emailInput = rootView.findViewById<EditText>(R.id.emailInput)
        val passwordInput = rootView.findViewById<EditText>(R.id.passwordInput)

        loginButton.setOnClickListener {
            navController = rootView.findNavController()
            if (isValid(emailInput, passwordInput)) {
                loginUser(getEditTextValue(emailInput), getEditTextValue(passwordInput))
            }
        }
        signupLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_login_to_signUp)
        }

        return rootView
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate(R.id.action_login_to_profile)
            } else {
                Toast.makeText(activity, "Invalid email/password", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValid(emailInput: EditText, passwordInput: EditText): Boolean {
        val email = getEditTextValue(emailInput)
        val password = getEditTextValue(passwordInput)

        if (email.isEmpty()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Provide valid email address"
            emailInput.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return false
        }

        return true
    }

    private fun getEditTextValue(editText: EditText): String {
        return editText.text.toString().trim()
    }
}
