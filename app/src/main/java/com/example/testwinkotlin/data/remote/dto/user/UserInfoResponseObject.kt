package com.example.testwinkotlin.data.remote.dto.user

import com.example.testwinkotlin.domain.model.UserInfoModel


data class UserInfoResponseObject (
    var data: UserInfoResponseObjectData? = null
)

open class UserInfoResponseObjectData {
    var id = 0
    var name: String? = null
    var username: String? = null
    var email: String? = null
    var rol: String? = null
    var mypermits: ArrayList<String>? = null
}

fun UserInfoResponseObjectData.toUserInfoModel(): UserInfoModel {
    return UserInfoModel(
        id = this.id,
        name = this.name,
        username = this.username,
        email = this.email,
        rol = this.rol,
        mypermits = this.mypermits
    )
}