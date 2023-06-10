package com.example.testwinkotlin.domain.model

import com.google.gson.Gson

data class TokensAndCredentialsModel(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
    val password: String
    )

fun TokensAndCredentialsModel.toJson(): String {
    return Gson().toJson(this)
}

fun String.toTokensAndCredentialsModel(): TokensAndCredentialsModel {
    return Gson().fromJson(this, TokensAndCredentialsModel::class.java)
}