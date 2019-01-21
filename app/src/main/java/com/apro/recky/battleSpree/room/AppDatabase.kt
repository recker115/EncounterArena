package com.apro.recky.battleSpree.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apro.recky.battleSpree.models.EachNotification
import com.apro.recky.battleSpree.offlineRepository.NotificationDao

@Database(entities = [EachNotification::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao
}