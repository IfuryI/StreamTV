package com.example.streamtv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class Profile : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.profile, container, false)

        val editButton = rootView.findViewById<ImageView>(R.id.editButton)
        val logoutButton = rootView.findViewById<ImageView>(R.id.logoutButton)

        editButton.setOnClickListener {
            Toast.makeText(activity, "Editing profile in progress...", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            rootView.findNavController().navigate(R.id.action_profileFragment_to_login)
        }

        return rootView
    }
}