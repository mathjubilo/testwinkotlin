package com.example.testwinkotlin.data.repository

import android.app.Application
import com.example.testwinkotlin.data.local.api.DataStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.data.remote.dto.login.LoginResponseObject
import com.example.testwinkotlin.data.remote.dto.logout.LogoutRequestObject
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.domain.repository.ILogoutRepository
import retrofit2.Response
import javax.inject.Inject

class LogoutRepository
@Inject constructor(
    private val winApi: IWinApi
): ILogoutRepository {

    override suspend fun logout(logoutRequestObject: LogoutRequestObject): Response<Unit> {

        return winApi.logout(logoutRequestObject)
    }
}