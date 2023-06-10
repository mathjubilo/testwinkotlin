package com.example.testwinkotlin.utils.pushNotifications

import WINRemoteNotification
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.inditex.messgand.firebase.AbstractFirebaseReceiverService

class MessagingService : AbstractFirebaseReceiverService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("MessagingService", "push received")
        val notification: WINRemoteNotification? = WINRemoteNotification.winRemoteNotification(remoteMessage)
        ShowNotification(application, notification)
    }
}
