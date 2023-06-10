package com.example.testwinkotlin.presentation.incidentsView

import android.util.Log


class IncidentsEvent
constructor(
    var viewModel: IncidentsViewModel
) {

    /*fun set(event: IncidentsEvents) {

        Log.d("ActiveIncidentsViewModel", "Running event ${event}")
        viewModel.state = IncidentsState.loading

        when (event) {

            ActiveIncidentsEvents.getIsFirstTime -> {
                viewModel.getIsFirstTime()
            }
            ActiveIncidentsEvents.isFirstTimeTrue -> {
                isFirstTime = true
                set(ActiveIncidentsEvents.getTokensFromDevice)
            }
            ActiveIncidentsEvents.isFirstTimeFalse -> {
                isFirstTime = false
                set(ActiveIncidentsEvents.getBiometricLoginStatus)
            }
        }
    }

    fun navigateToLoginAndShowLoginForm() {
        IncidentsViewModel.initialEvent = LoginViewEvent.showLoginForm
        set(ActiveIncidentsEvents.navigateToLoginView)
    }*/
}

enum class ActiveIncidentsEvents {
    getIsFirstTime,
    isFirstTimeTrue,
    isFirstTimeFalse
}