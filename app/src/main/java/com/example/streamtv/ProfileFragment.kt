package com.example.streamtv

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class ProfileFragment : Fragment() {
    private var user: FirebaseUser? = null
    private lateinit var reference: DatabaseReference

    private var userID: String = ""

    private lateinit var avatar: ImageView

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

        avatar = rootView.findViewById(R.id.avatar)
        val username = rootView.findViewById<TextView>(R.id.username)
        val status = rootView.findViewById<TextView>(R.id.status)
        val about = rootView.findViewById<TextView>(R.id.aboutText)

        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<UserFragment>()
                if (user == null) {
                    Log.w("Profile", "Failed to get user data")
                    Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                username.text = user.login
                status.text = user.status ?: "no status"
                about.text = user.about ?: "User hasn't provided information about themselves"
                Glide
                    .with(this@ProfileFragment)
                    .load(user.avatarURL ?: R.drawable.ic_default_avatar)
                    .dontTransform()
                    .into(avatar)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Profile", "Failed to get user data")
                Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG).show()
            }
        })

        val editButton = rootView.findViewById<ImageView>(R.id.editButton)
        val logoutButton = rootView.findViewById<ImageView>(R.id.logoutButton)
        val changeAvatarButton = rootView.findViewById<ImageView>(R.id.changeAvatar)

        editButton.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_profileFragment_to_editProfile)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            rootView.findNavController().navigate(R.id.action_profileFragment_to_login)
        }

        changeAvatarButton.setOnClickListener {
            selectImage()
        }

        return rootView
    }

    private val choosePicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide
                    .with(this)
                    .load(uri)
                    .into(avatar)
                // TODO: upload image on the server
            }
        }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Choose from Gallery" -> {
                    choosePicture.launch("image/*")
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
}
