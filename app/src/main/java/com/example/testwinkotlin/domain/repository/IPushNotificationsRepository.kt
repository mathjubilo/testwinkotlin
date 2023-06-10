package com.example.testwinkotlin.domain.repository

interface IPushNotificationsRepository {
    suspend fun getDeviceKeyFromStorage()
    suspend fun saveDeviceKey()
    suspend fun register()
    suspend fun unregister()
}