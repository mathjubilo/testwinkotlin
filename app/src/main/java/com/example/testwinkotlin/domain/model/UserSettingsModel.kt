package com.example.testwinkotlin.domain.model

import com.example.testwinkotlin.data.remote.dto.user.UserSettingsRequestObject
import com.google.gson.Gson

class UserSettingsModel {
    var notificationsSettings: SettingsType? = null
    var visualizationSettings: SettingsType? = null
    var lux: Boolean? = null
    /*var key: String? = null
    var value: String? = null*/

    companion object {
        val notificationCritique = "6"
        val notificationHigh = "5"
        val notificationMedium = "4"
        val notificationLow = "3"

        val visualizationCritique = "10"
        val visualizationHigh = "9"
        val visualizationMedium = "8"
        val visualizationLow = "7"

        val luxTheme = "13"
    }
}

fun UserSettingsModel.toJson(): String {
    return Gson().toJson(this)
}

fun UserSettingsModel.toUserSettingsRequestObject(): UserSettingsRequestObject {
    return UserSettingsRequestObject()
}

class SettingsType {
    var critique: Boolean? = null
    var high: Boolean? = null
    var medium: Boolean? = null
    var low: Boolean? = null

    fun isAllTrue(): Boolean {
        return critique == true && high == true && medium == true && low == true
    }
}


/*

struct Settings {
    var notifications: SettingsType
    var visualization: SettingsType
    var lux: Bool
}

struct SettingsType {
    var critique: Bool
    var high: Bool
    var medium: Bool
    var low: Bool

    func allTrue() -> Bool {
        return critique && high && medium && low
    }
}

extension Settings {
    static func from(request: [SettingsResponse]) -> Settings {
        func get(key: SettingsKeys, defaultValue: Bool) -> Bool {
            if let result = request.filter({ $0.key == key.rawValue }).first {
                return result.value.lowercased() == "true"
            }
            return defaultValue
        }

        let notif = SettingsType(critique: get(key: .notificationCritique, defaultValue: false),
                                 high: get(key: .notificationHigh, defaultValue: false),
                                 medium: get(key: .notificationMedium, defaultValue: false),
                                 low: get(key: .notificationLow, defaultValue: false))
        let visual = SettingsType(critique: get(key: .visualizationCritique, defaultValue: false),
                                  high: get(key: .visualizationHigh, defaultValue: false),
                                  medium: get(key: .visualizationMedium, defaultValue: false),
                                  low: get(key: .visualizationLow, defaultValue: false))

        let lux = get(key: .luxTheme, defaultValue: true)

        return Settings(notifications: notif, visualization: visual, lux: lux)
    }
}

extension Settings {
     func toRequest() -> [SettingsRequest] {
        return [
            SettingsRequest(key: SettingsKeys.notificationCritique.rawValue, value: String(notifications.critique)),
            SettingsRequest(key: SettingsKeys.notificationHigh.rawValue, value: String(notifications.high)),
            SettingsRequest(key: SettingsKeys.notificationMedium.rawValue, value: String(notifications.medium)),
            SettingsRequest(key: SettingsKeys.notificationLow.rawValue, value: String(notifications.low)),
            SettingsRequest(key: SettingsKeys.visualizationCritique.rawValue, value: String(visualization.critique)),
            SettingsRequest(key: SettingsKeys.visualizationHigh.rawValue, value: String(visualization.high)),
            SettingsRequest(key: SettingsKeys.visualizationMedium.rawValue, value: String(visualization.medium)),
            SettingsRequest(key: SettingsKeys.visualizationLow.rawValue, value: String(visualization.low)),
            SettingsRequest(key: SettingsKeys.luxTheme.rawValue, value: String(lux))
        ]
    }
}

enum SettingsKeys: String {
    case notificationCritique = "6"
    case notificationHigh = "5"
    case notificationMedium = "4"
    case notificationLow = "3"

    case visualizationCritique = "10"
    case visualizationHigh = "9"
    case visualizationMedium = "8"
    case visualizationLow = "7"

    case luxTheme = "13"
}

 */