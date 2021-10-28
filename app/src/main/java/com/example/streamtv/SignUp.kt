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

        signupButton.setOnClickListener {
            navController = rootView.findNavController()
            val login = rootView.findViewById<EditText>(R.id.loginInput).text.toString().trim()
            val email = rootView.findViewById<EditText>(R.id.emailInput).text.toString().trim()
            val password = rootView.findViewById<EditText>(R.id.passwordInput).text.toString().trim()
            createUser(login, email, password)
        }
        loginLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_signUp_to_login)
        }
        return rootView
    }

    private fun createUser(login: String, email: String, password: String) {
        // TODO: validate data
        // if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("SignUp", "createUserWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.uid).setValue(User(login, email))
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    navController.navigate(R.id.action_signUp_to_profile)
                                }
                            }
                    }
                } else {
                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    // TODO: show error
                }
            }
    }
}
