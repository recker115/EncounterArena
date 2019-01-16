package com.example.shantunu.encounterarena.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    private var notificationBuilder: NotificationCompat.Builder ?= null
    private var notificationManager: NotificationManager? = null
    private var largeBitmap: Bitmap? = null
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
//        Log.d( "From: " + p0?.from)
//        Log.d( "From: " + p0.getFrom())
        // Check if message contains a data payload.
        p0?.let {
//            if (p0.data.isNotEmpty()) {
                largeBitmap = BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.rifle_colored
                )
                if (p0.data["type"].equals("room", ignoreCase = true)) run {
                    val title = p0.data["noti_title"]
                    val body = "Room Id- " + p0.data["RoomId"] + "\n" + "Password :- " +  p0.data["Password"]

                    Log.e("RoomId",  p0.data["RoomId"])
                    Log.e("Password",  p0.data["Password"])

                    initNotification(title, body)
                    notifyRoomId()
//                }
            }
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        postDeviceToken(p0)
    }

    private fun postDeviceToken(p0: String?) {

        var currentUUID = AppClass.getAppInstance()?.getFirebaseAuth()?.currentUser?.uid

        currentUUID?.let { it->  {
            p0?.let {p0->{
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)?.child(it)
                    ?.child(Constants.FCM_TOKEN)?.setValue(p0) }
            } }
        }

    }

    fun initNotification(title: String?, body: String?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel: NotificationChannel? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(
                    "getRoomId",
                    "EncounterArena channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel!!.description = "Getting snap FROM friends !"
            }

            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager?.createNotificationChannel(channel!!)
                }
            }
        }

        notificationBuilder = NotificationCompat.Builder(this, "getRoomId")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder
            ?.setCategory(NotificationCompat.CATEGORY_PROMO)
                ?.setSmallIcon(R.mipmap.game_black)
                ?.setLargeIcon(largeBitmap)
                ?.setStyle(NotificationCompat.BigTextStyle().bigText(body))
                ?.setPriority(NotificationCompat.PRIORITY_MAX)
                ?.setContentTitle(title)
                ?.setContentText(body)
                ?.setAutoCancel(true)
        }
    }

    private fun notifyRoomId() {
//        notificationBuilder.setContentIntent(notifyPendingIntent)
        notificationManager?.notify(Constants.NOTIFICATION_ROOM, notificationBuilder?.build())
    }
}