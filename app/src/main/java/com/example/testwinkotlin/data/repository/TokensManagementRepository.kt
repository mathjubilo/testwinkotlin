package com.example.testwinkotlin.data.repository

import com.example.testwinkotlin.data.local.api.BiometricsManagerApi
import com.example.testwinkotlin.data.local.api.IDataStoreApi
import com.example.testwinkotlin.data.local.api.IKeyStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.dto.tokensManagement.RefreshTokenRequestObject
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import retrofit2.Response
import javax.inject.Inject

class TokensManagementRepository
@Inject constructor(
    private val winApi: IWinApi,
    private val keyStoreApi: IKeyStoreApi,
    private val dataStoreApi: IDataStoreApi
): ITokensManagementRepository {
    override suspend fun refreshToken(refreshToken: String): Response<Unit> {
        var refreshTokenRequestObject = RefreshTokenRequestObject(
            refresh_token = refreshToken
        )
        return winApi.renewToken(refreshTokenRequestObject)
    }

    override suspend fun encryptTokensAndCredentials(tokensAndCredentialsJson: String): String {

        return keyStoreApi.encrypt(
            data = tokensAndCredentialsJson,
            forKey = "tokensAndUsername",
            withBiometricAuth = false
        )
    }

    override suspend fun decryptTokensAndCredentials(encryptedString: String): String {
        return keyStoreApi.decrypt(
            data = encryptedString,
            forKey = "tokensAndUsername",
            withBiometricAuth = false
        )
    }

    override suspend fun saveEncryptedTokensToDevice(value: String): String {
        dataStoreApi.saveString(
            "tokensAndUsername",
            value
        )
        dataStoreApi.saveString(
            "credentials",
            value
        )
        return dataStoreApi.getString(forKey = "tokensAndUsername")
    }

    override suspend fun getTokensFromDevice(): String {
        return dataStoreApi.getString("tokensAndUsername")
    }

    override suspend fun deleteTokensFromDevice(): String {
        dataStoreApi.saveString(
            "tokensAndUsername",
            ""
        )
        return dataStoreApi.getString(forKey = "tokensAndUsername")
    }

    override suspend fun getCredentialsFromDevice(): String {
        return dataStoreApi.getString("credentials")
    }
}