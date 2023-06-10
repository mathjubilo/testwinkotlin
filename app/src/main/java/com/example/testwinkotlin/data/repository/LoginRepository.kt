package com.example.testwinkotlin.data.repository

import android.app.Application
import com.example.testwinkotlin.data.local.api.DataStoreApi
import com.example.testwinkotlin.data.local.api.IDataStoreApi
import com.example.testwinkotlin.data.local.api.IKeyStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.data.remote.dto.login.LoginResponseObject
import com.example.testwinkotlin.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class LoginRepository
@Inject constructor(
    private val application: Application,
    private val winApi: IWinApi,
    private val dataStoreApi: DataStoreApi
): ILoginRepository {

    override suspend fun login(loginRequestObject: LoginRequestObject): Response<LoginResponseObject> {

        return winApi.login(loginRequestObject)
    }

    override suspend fun getIsFirstTime(): Boolean {
        return dataStoreApi.getBoolean(forKey = "isFirstTime")
    }

    override suspend fun setIsFirstTime(value: Boolean): Boolean {
        dataStoreApi.saveBoolean(
            "isFirstTime",
            value
        )
        return dataStoreApi.getBoolean(forKey = "isFirstTime")
    }

    override suspend fun getBiometricLoginStatus(): Boolean {
        return dataStoreApi.getBoolean(forKey = "biometricLoginStatus")
    }

    override suspend fun setBiometricLoginStatus(value: Boolean): Boolean {
        dataStoreApi.saveBoolean(
            "biometricLoginStatus",
            value
        )
        return dataStoreApi.getBoolean(forKey = "biometricLoginStatus")
    }
}