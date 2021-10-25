package com.example.streamtv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController

class SignUp : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.sign_up, container, false)

        val signupButton = rootView.findViewById<Button>(R.id.signupButton)
        val loginLink = rootView.findViewById<TextView>(R.id.loginLink)

        signupButton.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_signUp_to_profile)
        }
        loginLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_signUp_to_login)
        }
        return rootView
    }
}
