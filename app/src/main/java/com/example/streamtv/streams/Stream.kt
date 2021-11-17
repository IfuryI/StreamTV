package com.example.streamtv.streams

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Stream(
    var title: String? = null,
    var author: String? = null,
    var watching: Number? = 0,
    var caption: String? = null,
)
