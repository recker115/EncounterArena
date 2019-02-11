package com.apro.recky.battleSpree

import android.app.Application
import androidx.room.Room
import com.apro.recky.battleSpree.room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppClass : Application() {

    var mRealTimeDatabase : DatabaseReference ?= null
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

    fun generateUuid(){
        currentUuId = getFirebaseAuth().currentUser?.uid.toString()
    }

    fun getRealTimeDatabase() : DatabaseReference {
        return if (mRealTimeDatabase != null) {
            mRealTimeDatabase as DatabaseReference
        } else {
            FirebaseDatabase.getInstance().reference
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