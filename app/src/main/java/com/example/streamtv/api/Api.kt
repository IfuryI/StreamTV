package com.example.streamtv.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private const val BASE_URL = "https://6194af159b1e780017ca20da.mockapi.io/api/v1/"

    private fun retrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val streamsService: StreamsApiService by lazy {
        retrofit().create(StreamsApiService::class.java)
    }
}