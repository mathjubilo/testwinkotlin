package com.example.testwinkotlin.presentation.login

class LoginViewState {

    companion object {

        var loading: String = "loading"
        var error: String = "isShowingError"
        val navigateToAppView = "navigateToAppView"
        var navigateToLaunchScreenView = "navigateToLaunchScreenView"
        val loginForm = "loginForm"
        val loginError = "loginError"
    }
}