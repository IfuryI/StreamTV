package com.example.streamtv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController

class Login : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.login, container, false)

        val loginButton = rootView.findViewById<Button>(R.id.loginButton)
        val signupLink = rootView.findViewById<TextView>(R.id.signupLink)

        loginButton.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_login_to_profile)
        }
        signupLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_login_to_signUp)
        }

        return rootView
    }
}
