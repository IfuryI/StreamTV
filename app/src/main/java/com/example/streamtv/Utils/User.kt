package com.example.streamtv.Utils

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var login: String? = null,
    var email: String? = null,
    var status: String? = null,
    var about: String? = null,
    var avatarURL: String? = null
)
