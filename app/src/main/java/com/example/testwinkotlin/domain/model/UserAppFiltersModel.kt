package com.example.testwinkotlin.domain.model

import com.example.testwinkotlin.data.local.dto.UserFilters
import com.example.testwinkotlin.data.remote.dto.user.toJson
import com.google.gson.Gson

data class UserAppFiltersModel (
    var activeIncidentsFilters: UserFilters? = null,
    var followedIncidentsFilters: UserFilters? = null,
    var distributionCentersFilters: UserFilters? = null
)

fun UserAppFiltersModel.updateAppFilters() {

}

fun UserAppFiltersModel.toJson(): String {
    return return Gson().toJson(this)
}