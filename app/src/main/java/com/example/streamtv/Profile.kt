package com.example.streamtv

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Profile : Fragment() {
    private var user: FirebaseUser? = null
    private lateinit var reference: DatabaseReference

    private var userID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user?.uid ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.profile, container, false)

        if (user == null) {
            rootView.findNavController().navigate(R.id.action_profileFragment_to_login)
        }

        val avatar = rootView.findViewById<ImageView>(R.id.avatar)
        val username = rootView.findViewById<TextView>(R.id.username)
        val status = rootView.findViewById<TextView>(R.id.status)
        val about = rootView.findViewById<TextView>(R.id.aboutText)

        reference.child(userID).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user == null) {
                    Log.w("Profile", "Failed to get user data")
                    Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG).show()
                    return
                }
                username.text = user.login
                status.text = user.status ?: "no status"
                about.text = user.about ?: "User hasn't provided information about themselves"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Profile", "Failed to get user data")
                Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG).show()
            }
        })

        val editButton = rootView.findViewById<ImageView>(R.id.editButton)
        val logoutButton = rootView.findViewById<ImageView>(R.id.logoutButton)

        editButton.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_profileFragment_to_editProfile)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            rootView.findNavController().navigate(R.id.action_profileFragment_to_login)
        }

        return rootView
    }
}