package com.example.testwinkotlin.data.local.dto

data class UserAppFiltersResponseObject (

    var activeIncidentsFilters: UserFilters? = null,
    var followedIncidentsFilters: UserFilters? = null,
    var distributionCentersFilters: UserFilters? = null
)