package com.example.testwinkotlin.data.repository

import com.example.testwinkotlin.data.local.api.IDataStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.dto.tokensManagement.RefreshTokenRequestObject
import com.example.testwinkotlin.data.remote.dto.user.UserInfoResponseObject
import com.example.testwinkotlin.data.remote.dto.user.UserSettingsResponseObject
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.domain.repository.IUserRepository
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val winApi: IWinApi,
    private val dataStoreApi: IDataStoreApi
): IUserRepository {

    override suspend fun getUserSettings(): Response<UserSettingsResponseObject> {
        return winApi.getUserSettings()
    }

    override suspend fun getUserInfo(): Response<UserInfoResponseObject?>? {
        return winApi.getUserInfo()
    }

    override suspend fun getLocalUserInfo(): String {
        return dataStoreApi.getString(forKey = "userInfo")
    }

    override suspend fun saveLocalUserInfo(value: String): String {
        dataStoreApi.saveString(
            "userInfo",
            value
        )
        return dataStoreApi.getString(forKey = "userInfo")
    }

    override suspend fun getLocalUserSettings(): String {
        return dataStoreApi.getString(forKey = "userSettings")
    }

    override suspend fun saveLocalUserSettings(value: String): String {
        dataStoreApi.saveString(
            "userSettings",
            value
        )
        return dataStoreApi.getString(forKey = "userSettings")
    }

    override suspend fun getUserAppFilters(): String {
        return dataStoreApi.getString(forKey = "userAppFilters")
    }

    override suspend fun saveUserAppFilters(value: String): String {
        dataStoreApi.saveString(
            "userAppFilters",
            value
        )
        return dataStoreApi.getString(forKey = "userAppFilters")
    }
}