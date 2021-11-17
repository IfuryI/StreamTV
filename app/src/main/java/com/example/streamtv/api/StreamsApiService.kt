package com.example.streamtv.api

import com.example.streamtv.streams.Stream
import retrofit2.Call
import retrofit2.http.GET

interface StreamsApiService {
    @GET("/streams")
    fun getStreams(): Call<List<Stream>>
}
