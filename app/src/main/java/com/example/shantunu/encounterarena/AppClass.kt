package com.example.shantunu.encounterarena

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppClass : Application() {

    var mRealTimeDatabase : DatabaseReference ?= null
    var mFirebaseAuth : FirebaseAuth ?= null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        mRealTimeDatabase = FirebaseDatabase.getInstance().reference
        APPINSTANCE = this
    }

    fun getRealTimeDatabase() : DatabaseReference {
        mRealTimeDatabase?.let {
            return mRealTimeDatabase as DatabaseReference
        }
    }

    fun getFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    companion object {
        var APPINSTANCE : AppClass ?= null
        fun getAppInstance() : AppClass? {
            return APPINSTANCE
        }
    }
}