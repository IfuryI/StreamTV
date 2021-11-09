package com.example.streamtv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.streamtv.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bottomNavigationView: BottomNavigationView? = null
    private var navController: NavController? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigation()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth?.currentUser
        if (currentUser != null) {
            navController?.navigate(R.id.profileFragment)
        }
    }

    private fun setupNavigation() {
        bottomNavigationView = binding.bottomNav
        navController = findNavController(R.id.nav_host_fragment)
        navController?.let {
            bottomNavigationView?.setupWithNavController(it)
            it.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.login, R.id.signUp -> hideBottomNav()
                    else -> showBottomNav()
                }
            }
        }
    }

    private fun showBottomNav() {
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNavigationView?.visibility = View.INVISIBLE
    }
}
