package com.example.streamtv

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val login: String? = null, val email: String? = null,
    val status: String? = null, val about: String? = null
) {}
