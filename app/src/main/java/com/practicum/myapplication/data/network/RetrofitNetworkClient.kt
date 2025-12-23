package com.practicum.myapplication.data.network

import com.practicum.myapplication.creator.Storage
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(dto: Any): TracksSearchResponse {
        if (dto is TracksSearchRequest) {
            val searchList = storage.search(dto.expression)
            return TracksSearchResponse(searchList).apply { resultCode = 200 }
        } else {
            return TracksSearchResponse(emptyList()).apply { resultCode = 400 }
        }
    }
}