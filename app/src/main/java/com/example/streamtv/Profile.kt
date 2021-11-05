package com.example.streamtv

import android.Manifest
import android.R.attr
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.content.DialogInterface
import android.database.Cursor
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.FileDescriptor
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable

import androidx.core.content.ContextCompat
import androidx.annotation.NonNull
import java.io.File
import android.R.attr.data
import androidx.core.app.ActivityCompat.startActivityForResult
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception
import android.R.attr.path
import android.os.Parcelable

import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter


class Profile : Fragment() {
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
                val user = snapshot.getValue<User>()
                if (user == null) {
                    Log.w("Profile", "Failed to get user data")
                    Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                username.text = user.login
                status.text = user.status ?: "no status"
                about.text = user.about ?: "User hasn't provided information about themselves"
                Glide.with(requireActivity())
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
            checkPermissions(requireActivity())
        }

        return rootView
    }

    private fun checkPermissions(context: Context) {
        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            selectImage(context)
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }
    }

    private fun selectImage(context: Context) {
        FishBun
            .with(this)
            .setImageAdapter(CoilAdapter())
            .setMaxCount(1)
            .startAlbum()
//        val options = arrayOf<CharSequence>("Choose from Gallery", "Cancel")
//        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
//        builder.setTitle("Choose your profile picture")
//        builder.setItems(options) { dialog, item ->
//            when {
//                options[item] == "Choose from Gallery" -> {
//                    val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    startActivityForResult(i, 2)
//                }
//                options[item] == "Cancel" -> {
//                    dialog.dismiss()
//                }
//            }
//        }
//        builder.show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
//            val selectedImage: Uri = data.data ?: return
//            Log.v("AVATAR", selectedImage.toString())
//            avatar.setImageURI(selectedImage)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FishBun.FISHBUN_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                val path = data?.getParcelableArrayListExtra<Parcelable>(INTENT_PATH)
                val pic = path?.get(0) ?: return
                Log.v("HERE", pic.toString())
                Glide
                    .with(this)
                    .load(pic)
                    .into(avatar)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            var allow = false
            for (granted in grantResults) {
                if (granted == PackageManager.PERMISSION_GRANTED) {
                    allow = true
                }
            }
            if (allow) {
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_LONG).show()
            }
        }
    }
}
