package com.practicum.myapplication.data.network

import android.util.Log
import com.practicum.myapplication.data.dto.BaseResponse
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.NetworkClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl : NetworkClient {

    private val itunesApi: ItunesApi

    init {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("iTunesAPI", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        itunesApi = retrofit.create(ItunesApi::class.java)
    }

    override suspend fun doRequest(dto: Any): BaseResponse {
        return when (dto) {
            is TracksSearchRequest -> {
                try {
                    val response = itunesApi.searchTracks(
                        term = dto.expression,
                        entity = "song",
                        media = "music"
                    )
                    Log.d("iTunesAPI", "Response results count: ${response.results.size}")
                    response.resultCode = 200
                    response
                } catch (e: Exception) {
                    Log.e("iTunesAPI", "Error: ${e.message}", e)
                    BaseResponse().apply { resultCode = 500 }
                }
            }

            else -> BaseResponse().apply { resultCode = 400 }
        }
    }
}