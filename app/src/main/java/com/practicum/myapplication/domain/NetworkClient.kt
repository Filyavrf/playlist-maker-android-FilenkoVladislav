package com.practicum.myapplication.domain

import com.practicum.myapplication.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}