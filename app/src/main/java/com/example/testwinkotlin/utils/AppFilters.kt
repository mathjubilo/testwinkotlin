package com.example.testwinkotlin.utils

import com.example.testwinkotlin.data.local.dto.UserFilters
import com.example.testwinkotlin.domain.model.UserAppFiltersModel
import com.example.testwinkotlin.domain.model.toJson
import com.google.gson.Gson

class AppFilters {
    companion object {
        var activeIncidentsFilters = UserFilters(
            false,
            false,
            false,
            false,
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            "",
            "testingSearchActiveIncidents",
            ArrayList(),
            ArrayList(),
            ArrayList()
        )
        var followedIncidentsFilters = UserFilters(
            false,
            false,
            false,
            false,
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            "",
            "testingSearchFollowedIncidents",
            ArrayList(),
            ArrayList(),
            ArrayList()
        )
        var distributionCentersFilters = UserFilters(
            false,
            false,
            false,
            false,
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            ArrayList(),
            "",
            "testingSearchDistributionCenters",
            ArrayList(),
            ArrayList(),
            ArrayList()
        )
    }
}

fun AppFilters.toJson(): String {
    return Gson().toJson(this)
}

fun AppFilters.Companion.toUserAppFiltersModel(): UserAppFiltersModel {
    val userAppFiltersModel = UserAppFiltersModel(
        AppFilters.activeIncidentsFilters,
        AppFilters.followedIncidentsFilters,
        AppFilters.distributionCentersFilters
    )
    return userAppFiltersModel
}