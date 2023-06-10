package com.example.testwinkotlin.data.remote.dto.user

import com.example.testwinkotlin.domain.model.SettingsType
import com.example.testwinkotlin.domain.model.UserSettingsModel
import com.example.testwinkotlin.domain.model.toJson
import com.google.gson.Gson

class UserSettingsResponseObject {
    var data: ArrayList<SettingResponseObject>? = null
}

open class SettingResponseObject {
    var key: String? = null
    var value: String? = null
}

fun ArrayList<SettingResponseObject>.toJson(): String {
    return Gson().toJson(this)
}


fun ArrayList<SettingResponseObject>.toUserSettingsModel(): UserSettingsModel {

    var userSettingsModel = UserSettingsModel()
    userSettingsModel.notificationsSettings = SettingsType()
    userSettingsModel.visualizationSettings = SettingsType()

    this.map { userSettingDto ->
        when(userSettingDto.key) {
            UserSettingsModel.notificationCritique -> {
                userSettingsModel.notificationsSettings?.critique = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.notificationHigh -> {
                userSettingsModel.notificationsSettings?.high = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.notificationMedium -> {
                userSettingsModel.notificationsSettings?.medium = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.notificationLow -> {
                userSettingsModel.notificationsSettings?.low = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.visualizationCritique -> {
                userSettingsModel.visualizationSettings?.critique = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.visualizationHigh -> {
                userSettingsModel.visualizationSettings?.high = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.visualizationMedium -> {
                userSettingsModel.visualizationSettings?.medium = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.visualizationLow -> {
                userSettingsModel.visualizationSettings?.low = userSettingDto.value.toBoolean()
            }
            UserSettingsModel.luxTheme -> {
                userSettingsModel.lux = userSettingDto.value.toBoolean()
            }
        }
    }

    return userSettingsModel
}