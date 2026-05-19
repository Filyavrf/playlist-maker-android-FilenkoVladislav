package com.practicum.myapplication.domain

import com.practicum.myapplication.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}