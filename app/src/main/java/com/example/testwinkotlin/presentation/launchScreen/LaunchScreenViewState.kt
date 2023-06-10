package com.example.testwinkotlin.presentation.launchScreen

enum class LaunchScreenViewState {
    loading,
    error,
    navigateToAppView,
    navigateToLoginView,
    showEncryptBiometricWindow,
    showDecryptBiometricWindow,
    encryptWithoutAsk,
    registerToPushNotifications,
    unregisterToPushNotifications,
    showBiometricsSettings,
    loginWithBiometrics
}