package com.example.shantunu.encounterarena.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shantunu.encounterarena.models.EachNotification
import com.example.shantunu.encounterarena.offlineRepository.NotificationDao

@Database(entities = [EachNotification::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao
}