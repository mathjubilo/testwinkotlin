package com.example.testwinkotlin.data.remote.api

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.inditex.messgand.manager.Messaging
import com.inditex.msgcomad.device.Device
import com.inditex.msgcomad.device.DeviceConfig
import com.inditex.msgcomad.error.Error
import com.inditex.msgcomad.manager.BaseMessaging

class PushServiceApi(
    val activity: Activity,
    val alias: String,
    val pushUrl: String,
    val deviceKeyFromStorage: String
) {
    private val device: Device
    var deviceConfig: DeviceConfig

    init {
        Log.d("PushServiceApi", "alias: $alias, pushUrl: $pushUrl")
        deviceConfig = DeviceConfig()
        deviceConfig.setServerUri(Uri.parse(pushUrl))
        deviceConfig.setAppKey("com.inditex.sgawinmb")
        deviceConfig.setEndpointKey("com.inditex.winandroid")
        deviceConfig.setServiceSenderId("1029821155066")

        val deviceKeyExists = !deviceKeyFromStorage.isEmpty()

        if (deviceKeyExists) {
            deviceConfig.setDeviceKey(deviceKeyFromStorage)
        }

        device = Device(deviceConfig)
        device.setAlias(this.alias)
    }

    fun delete(
        onSuccess: () -> Unit,
        onError: (com.inditex.msgcomad.error.Error?) -> Unit
    ) {
        Messaging.deleteDevice(
            activity,

            object : BaseMessaging.SuccessListener {
                override fun onSuccess() {
                    onSuccess()
                }
            },

            object : BaseMessaging.ErrorListener {
                override fun onErrorResponse(p0: com.inditex.msgcomad.error.Error?) {
                    onError(p0)
                }
            })
    }

    fun register(
        onConnected: (String?) -> Unit,
        onConnectionFailed: (Error?) -> Unit
    ) {
        Messaging.init(
            activity,
            device,
            object: BaseMessaging.ConnectionListener {
                override fun onConnected(p0: String?) {
                    onConnected(p0)
                }

                override fun onConnectionFailed(p0: Error?) {
                    onConnectionFailed(p0)
                }

            }
        )
    }

    fun registerState(
        onEvent: (String) -> Unit
    ) {
        Messaging.setInfoStateListener(
            object: BaseMessaging.InfoStateListener {
                override fun onEvent(p0: String?) {
                    onEvent(p0)
                }
            }
        )
    }
}