package com.example.shantunu.encounterarena

import android.app.Application
import androidx.room.Room
import com.example.shantunu.encounterarena.room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppClass : Application() {

    var mRealTimeDatabase : DatabaseReference ?= null
    var mFirebaseAuth : FirebaseAuth ?= null
    var currentUuId : String = ""
    var appDatabase : AppDatabase ?= null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        mRealTimeDatabase = FirebaseDatabase.getInstance().reference
        APPINSTANCE = this
        currentUuId = getFirebaseAuth().currentUser?.uid.toString()
        appDatabase = Room.databaseBuilder(this@AppClass, AppDatabase::class.java, Constants.DATABASE_NAME).build()
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