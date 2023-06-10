package com.example.testwinkotlin.domain.repository

import com.example.testwinkotlin.data.remote.dto.logout.LogoutRequestObject
import retrofit2.Response

interface ILogoutRepository {
    suspend fun logout(logoutRequestObject: LogoutRequestObject): Response<Unit>
}