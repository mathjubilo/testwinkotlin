import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonArray
import com.google.gson.JsonParser

data class WINRemoteNotification(
    val key: String?,
    val deviceKey: String?,
    val content: JsonArray?,
    val alert: String?,
    val sound: String?,
    val badge: Int?,
    val link: String?,
    val title: String?,
) {

    companion object {
        fun winRemoteNotification(remoteMessage: RemoteMessage): WINRemoteNotification? {
            val extras = remoteMessage.data
            return if (extras != null && !extras.isEmpty()) {
                createRemoteNotification(
                    extras["key"],
                    extras["deviceKey"],
                    extras["content"], extras["alert"], extras["sound"],
                    extras["badge"], extras["link"], extras["title"])
            } else {
                Log.d("WINRemoteNotification", "is null")
                null
            }
        }

        private fun createRemoteNotification(
            key: String?,
            deviceKey: String?,
            contentString: String?,
            alert: String?,
            sound: String?,
            badgeString: String?,
            link: String?,
            title: String?,
        ): WINRemoteNotification? {
            var badge: Int? = null
            var content = JsonArray()
            try {
                try {
                    if (badgeString != null) {
                        badge = badgeString.toInt()
                    }
                } catch (var10: NumberFormatException) {
                    Log.d("WINRemoteNotification", "Badge is not numeric")
                }
                if (contentString != null) {
                    Log.d("WINRemoteNotification", "Content is ${contentString}")
                    val json = JsonParser.parseString(contentString)
                    if (json.isJsonArray) {
                        content = json.asJsonArray
                    } else {
                        content.add(json)
                    }
                }
            } catch (var11: RuntimeException) {
                return null
            }

            return WINRemoteNotification(
                key,
                deviceKey,
                content,
                alert,
                sound,
                badge,
                link,
                title
            )
        }
    }
}
