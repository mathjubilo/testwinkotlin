package com.example.testwinkotlin.data.repository

import com.example.testwinkotlin.MainActivity
import com.example.testwinkotlin.data.remote.api.PushServiceApi
import com.example.testwinkotlin.domain.repository.IPushNotificationsRepository
import com.inditex.msgcomad.error.Error

class PushNotificationsRepository
    constructor(
        //private val pushServiceApi: PushServiceApi
    ): IPushNotificationsRepository {

    override suspend fun getDeviceKeyFromStorage() {

    }

    override suspend fun saveDeviceKey() {

    }

    override suspend fun register() {

    }

    override suspend fun unregister() {

    }
}