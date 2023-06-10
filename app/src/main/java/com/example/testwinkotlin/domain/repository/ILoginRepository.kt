package com.example.testwinkotlin.domain.repository

import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.data.remote.dto.login.LoginResponseObject
import retrofit2.Response

interface ILoginRepository {

    suspend fun login(loginRequestObject: LoginRequestObject): Response<LoginResponseObject>
    suspend fun getIsFirstTime(): Boolean
    suspend fun setIsFirstTime(value: Boolean): Boolean
    suspend fun getBiometricLoginStatus(): Boolean
    suspend fun setBiometricLoginStatus(value: Boolean): Boolean
}