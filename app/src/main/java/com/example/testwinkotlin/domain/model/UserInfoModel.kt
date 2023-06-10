package com.example.testwinkotlin.domain.model

import com.google.gson.Gson

data class UserInfoModel (
    var id: Int = 0,
    var name: String? = null,
    var username: String? = null,
    var email: String? = null,
    var rol: String? = null,
    var mypermits: ArrayList<String>? = null
)

fun UserInfoModel.toJson(): String {
    return Gson().toJson(this)
}

fun String.toUserInfoModel(): UserInfoModel {
    return Gson().fromJson(this, UserInfoModel::class.java)
}