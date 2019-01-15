package com.example.shantunu.encounterarena.fcm

import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
//        Log.d( "From: " + p0?.from)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        postDeviceToken(p0)    }

    private fun postDeviceToken(p0: String?) {

        var currentUUID = AppClass.getAppInstance()?.getFirebaseAuth()?.currentUser?.uid

        currentUUID?.let { it->  {
            p0?.let {p0->{
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)?.child(it)
                    ?.child(Constants.FCM_TOKEN)?.setValue(p0) }
            } }
        }

    }
}