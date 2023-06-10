package com.example.testwinkotlin.presentation.login

class LoginViewEvent {

    companion object {

        var navigateToAppView = "navigateToAppView"
        var doLogin = "login"
        var loginSuccess = "loginSuccess"
        var loginError = "loginError"
        var showErrorMessage = "showErrorMessage"
        var showLoginForm = "showLoginForm"
        var navigateToLaunchScreenView = "navigateToLaunchScreenView"
        var showBiometricsSettings = "showBiometricsSettings"
    }
}