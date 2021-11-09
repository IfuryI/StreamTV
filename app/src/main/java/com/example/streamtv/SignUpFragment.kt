package com.example.streamtv

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.streamtv.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {
    private var _binding: SignUpBinding? = null
    private var navController: NavController? = null
    private var auth: FirebaseAuth? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginInput = binding.loginInput
        val emailInput = binding.emailInput
        val passwordInput = binding.passwordInput

        binding.signupButton.setOnClickListener {
            navController = NavHostFragment.findNavController(this)
            if (isValid(loginInput, emailInput, passwordInput)) {
                createUser(getEditTextValue(loginInput), getEditTextValue(emailInput),
                    getEditTextValue(passwordInput))
            }
        }
        binding.loginLink.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_signUp_to_login)
        }
    }

    private fun createUser(login: String, email: String, password: String) {
        auth?.let {
            it.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = it.currentUser
                        if (user == null) {
                            Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.uid).setValue(User(login, email))
                            .addOnCompleteListener(requireActivity()) { dbtask ->
                                if (dbtask.isSuccessful) {
                                    Log.d("SignUp", "createUserWithEmail:success")
                                    navController?.navigate(R.id.action_signUp_to_profile)
                                } else {
                                    Toast.makeText(context, "Error creating user",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
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