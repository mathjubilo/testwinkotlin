package com.example.testwinkotlin.utils

import com.example.testwinkotlin.data.local.api.BiometricsApi
import com.example.testwinkotlin.data.local.api.DataStoreApi
import com.example.testwinkotlin.data.local.api.KeyStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.api.PushServiceApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class APITests
@Inject constructor(
    /*winApi: IWinApi,
    dataStoreApi: DataStoreApi,
    biometricsApi: BiometricsApi,
    keyStoreApi: KeyStoreApi,
    pushServiceApi: PushServiceApi*/
){

    @Inject lateinit var winApi: IWinApi
    @Inject lateinit var dataStoreApi: DataStoreApi
    @Inject lateinit var biometricsApi: BiometricsApi
    @Inject lateinit var keyStoreApi: KeyStoreApi
    @Inject lateinit var pushServiceApi: PushServiceApi

    init {
        GlobalScope.launch {
            //testIsFirstTime(dataStoreApi)
        }
    }

    suspend fun testIsFirstTime(dataStoreApi: DataStoreApi) {
        dataStoreApi.getBoolean("isFirstTime")
    }
}