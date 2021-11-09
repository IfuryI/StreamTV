package com.example.streamtv

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.streamtv.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private var _binding: LoginBinding? = null
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
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInput = binding.emailInput
        val passwordInput = binding.passwordInput

        binding.loginButton.setOnClickListener {
            navController = NavHostFragment.findNavController(this)
            if (isValid(emailInput, passwordInput)) {
                loginUser(getEditTextValue(emailInput), getEditTextValue(passwordInput))
            }
        }
        binding.signupLink.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_signUp)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController?.navigate(R.id.action_login_to_profile)
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
