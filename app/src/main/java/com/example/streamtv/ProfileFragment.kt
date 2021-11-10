package com.example.streamtv

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.streamtv.databinding.ProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class ProfileFragment : Fragment() {
    private var _binding: ProfileBinding? = null
    private var user: FirebaseUser? = null
    private var reference: DatabaseReference? = null

    private val binding get() = _binding!!

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
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (user == null) {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_login)
        }

        reference?.child(userID)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user == null) {
                    Log.w("Profile", "Failed to get user data")
                    Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                binding.username.text = user.login
                binding.status.text = if (!user.status.isNullOrBlank()) user.status else "no status"
                binding.aboutText.text = if (!user.about.isNullOrBlank()) user.about else
                    "User hasn't provided information about themselves"
                Glide
                    .with(this@ProfileFragment)
                    .load(user.avatarURL ?: R.drawable.ic_default_avatar)
                    .dontTransform()
                    .into(binding.avatar)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Profile", "Failed to get user data")
                Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                    .show()
            }
        })

        val editButton = binding.editButton
        val logoutButton = binding.logoutButton
        val changeAvatarButton = binding.changeAvatar

        editButton.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_editProfile)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_profileFragment_to_login)
        }

        changeAvatarButton.setOnClickListener {
            selectImage()
        }
    }

    private val choosePicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide
                    .with(this)
                    .load(uri)
                    .into(binding.avatar)
                // upload image on the server
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
