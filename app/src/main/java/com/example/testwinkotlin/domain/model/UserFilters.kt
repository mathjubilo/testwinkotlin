package com.example.testwinkotlin.data.local.dto

data class UserFilters(
    var filterPriorityCritic: Boolean? = null,
    var filterPriorityHigh: Boolean? = null,
    var filterPriorityMid: Boolean? = null,
    var filterPriorityLow: Boolean? = null,

    var filterTime: ArrayList<Int>? = null,
    var filterCenter: ArrayList<Int>? = null,
    var filterModule: ArrayList<Int>? = null,
    var filterCenterType: ArrayList<Int>? = null,
    var filterBusiness: ArrayList<Int>? = null,
    var filterPriority: ArrayList<Int>? = null,
    var filterIncidenceCod: String? = null,
    var filterSearch: String? = null,
    var filterState: ArrayList<Int>? = null,
    var filterStateWithoutSolved: ArrayList<Int>? = null,
    var filterStateWithoutSolvedFollowed: ArrayList<Int>? = null,
) {

    /*fun UserFilters.checkIfStateIsEmpty(): ArrayList<Int> {
        //assignedState();
        return if (filterState?.size == 0) {
            filterStateWithoutSolved.clear()
            filterStateWithoutSolved.add(1)
            filterStateWithoutSolved.add(3)
            filterStateWithoutSolved.add(2)
            filterStateWithoutSolved.add(9)
            filterStateWithoutSolved.add(8)
            filterStateWithoutSolved.add(10)
            filterStateWithoutSolved
        } else {
            filterState
        }
    }

    fun checkIfStateIsEmptyForFollowed(): ArrayList<Int> {
        //assignedState();
        return if (filterState.size == 0) {
            filterStateWithoutSolvedFollowed.clear()
            filterStateWithoutSolvedFollowed.add(1)
            filterStateWithoutSolvedFollowed.add(2)
            filterStateWithoutSolvedFollowed.add(3)
            filterStateWithoutSolvedFollowed.add(4)
            filterStateWithoutSolvedFollowed.add(5)
            filterStateWithoutSolvedFollowed.add(8)
            filterStateWithoutSolvedFollowed.add(9)
            filterStateWithoutSolvedFollowed.add(10)
            filterStateWithoutSolvedFollowed
        } else {
            filterState
        }
    }

    fun setDcenterIncidentOrderAsc(isAsc: Boolean) {
        if (isAsc == true) {
            dcenterIncidentOrderAsc = "ASC"
        } else {
            dcenterIncidentOrderAsc = "DESC"
        }
    }

    fun setOrderAlphabetically(isAlphabetically: Boolean) {
        if (isAlphabetically == true) {
            orderDistributionCenter = 0
        } else {
            orderDistributionCenter = 1
        }
    }

    fun getFilterForType(type: FiltersType?): ArrayList<Int> {
        var returnValue = ArrayList<Int>()
        when (type) {
            FiltersType.CENTER -> {
                returnValue = filterCenters
                returnValue = filterBusiness
                returnValue = filterModule
            }
            FiltersType.BUSINESS -> {
                returnValue = filterBusiness
                returnValue = filterModule
            }
            FiltersType.MODULE -> returnValue = filterModule
            FiltersType.PRIORITY -> {}
            FiltersType.CENTERTYPE -> returnValue = filterCenterType
            FiltersType.TIME -> {}
            FiltersType.STATE -> {}
            else -> {}
        }
        return returnValue
    }

    fun countBusiness(): Int {
        return filterBusiness.size
    }

    fun countCenter(): Int {
        return filterCenters.size
    }

    fun countModule(): Int {
        return filterModule.size
    }

    fun countTypeCenter(): Int {
        return filterCenterType.size
    }

    fun countPriority(): Int {
        val priorities = ArrayList<Boolean>()
        var result = 0
        for (priority in priorities) {
            if (priority == true) {
                result += 1
            }
        }
        return result
    }

    fun countState(): Int {
        return filterState.size
    }

    fun countTime(): Int {
        return filterTime.size
    }

    fun clean() {
        filterCenters.clear()
        filterModule.clear()
        filterCenterType.clear()
        filterBusiness.clear()
        filterPriority.clear()
        filterState.clear()
        //filterStateWithoutSolved.clear();
        filterTime.clear()
        filterIncidenceCod = ""
    }
    */
}

enum class FiltersType {
    CENTER, BUSINESS, MODULE, PRIORITY, CENTERTYPE, TIME, STATE
}
