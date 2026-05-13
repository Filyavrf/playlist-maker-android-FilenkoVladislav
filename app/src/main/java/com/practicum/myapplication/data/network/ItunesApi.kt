package com.practicum.myapplication.data.network

import com.practicum.myapplication.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
        @Query("media") media: String = "music"
    ): TracksSearchResponse
}