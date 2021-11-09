package com.example.streamtv

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.streamtv.databinding.EditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class EditProfileFragment : Fragment() {
    private var _binding: EditProfileBinding? = null
    private var user: FirebaseUser? = null
    private var reference: DatabaseReference? = null
    private var userID: String = ""
    private var userModel: User? = null

    private val binding get() = _binding!!

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
        _binding = EditProfileBinding.inflate(inflater, container, false)
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
                    Log.w("EditProfile", "Failed to get user data")
                    Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                userModel = user
                binding.loginInput.setText(user.login)
                binding.statusInput.setText(user.status ?: "")
                binding.aboutInput.setText(user.about ?: "")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("EditProfile", "Failed to get user data")
                Toast.makeText(activity, "Error getting profile data!", Toast.LENGTH_LONG)
                    .show()
            }
        })

        binding.saveButton.setOnClickListener {
            userModel?.let {
                it.login = binding.loginInput.text.toString()
                it.status = binding.statusInput.text.toString()
                it.about = binding.aboutInput.text.toString()
            }
            reference?.child(userID)?.setValue(userModel)
                ?.addOnCompleteListener(requireActivity()) { dbtask ->
                    if (dbtask.isSuccessful) {
                        Log.d("EditProfile", "updateUser:success")
                        NavHostFragment.findNavController(this)
                            .navigate(R.id.action_editProfile_to_profileFragment)
                    } else {
                        Log.w("EditProfile", "updateUser:failure", dbtask.exception)
                        Toast.makeText(context, "Error updating user profile",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}