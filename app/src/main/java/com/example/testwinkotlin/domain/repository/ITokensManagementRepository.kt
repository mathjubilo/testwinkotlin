package com.example.testwinkotlin.domain.repository

import retrofit2.Response

interface ITokensManagementRepository {
    suspend fun refreshToken(refreshToken: String): Response<Unit>
    suspend fun encryptTokensAndCredentials(tokensAndCredentialsJson: String): String
    suspend fun decryptTokensAndCredentials(encryptedString: String): String
    suspend fun saveEncryptedTokensToDevice(value: String): String
    suspend fun getTokensFromDevice(): String
    suspend fun deleteTokensFromDevice(): String
    suspend fun getCredentialsFromDevice(): String

}