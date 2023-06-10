package com.example.testwinkotlin.utils.pushNotifications

import WINRemoteNotification
import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.testwinkotlin.MainActivity
import com.google.gson.JsonArray
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

class ShowNotification(private val application: Application, notification: WINRemoteNotification?) {
    private val context: Context
    private var rand: Random? = null

    init {
        context = application.applicationContext
        try {
            rand = SecureRandom.getInstanceStrong()
        } catch (e: NoSuchAlgorithmException) {
            Log.d("DEBUG", e.message!!)
        }
        val notificationContentArray: JsonArray? = notification?.content
        if (notificationContentArray != null && !notificationContentArray.isEmpty) {
            val notificationContent = notificationContentArray[0].asJsonObject

            if (notificationContent["type"] != null) {
                val notificationType = notificationContent["type"].asString
                val isSilentNotification = notificationType == "refresh-token"
                val hasId = notificationContent["id"] != null
                if (!isSilentNotification) {
                    var incidentId = 0
                    var intent = Intent()
                    val notificationTitle: String? = notification?.title
                    val notificationBody: String? = notification?.alert
                    if (hasId) {
                        incidentId = notificationContent["id"].asInt
                        intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            //flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            putExtra("push", "true")
                            putExtra("incId", incidentId)
                        }
                    }
                    showNotification(incidentId, intent, notificationTitle, notificationBody)
                }
            }
        }
    }

    private fun showNotification(incidentId: Int, intent: Intent, title: String?, body: String?) {
        val randomValue = rand!!.nextInt() * 2
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, incidentId, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context,
                incidentId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(androidx.core.R.drawable.notification_bg_low)
            .setContentTitle("WIN Kotlin - $title")
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(randomValue, mBuilder.build())
    }
}