package com.example.testwinkotlin.domain.repository

import com.example.testwinkotlin.data.remote.dto.user.UserInfoResponseObject
import com.example.testwinkotlin.data.remote.dto.user.UserSettingsResponseObject
import retrofit2.Response

interface IUserRepository {
    suspend fun getUserSettings(): Response<UserSettingsResponseObject>
    suspend fun getUserInfo(): Response<UserInfoResponseObject?>?
    suspend fun getLocalUserInfo(): String
    suspend fun saveLocalUserInfo(value: String): String
    suspend fun getLocalUserSettings(): String
    suspend fun saveLocalUserSettings(value: String): String
    suspend fun getUserAppFilters(): String
    suspend fun saveUserAppFilters(value: String): String
}