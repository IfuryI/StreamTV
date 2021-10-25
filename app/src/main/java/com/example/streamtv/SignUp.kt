package com.example.streamtv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val loginLink = rootView.findViewById<TextView>(R.id.loginLink)
        loginLink.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_signUp_to_login)
        }
        return rootView
    }
}
