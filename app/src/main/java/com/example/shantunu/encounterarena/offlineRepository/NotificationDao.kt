package com.example.shantunu.encounterarena.offlineRepository

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shantunu.encounterarena.models.EachNotification

@Dao
abstract class NotificationDao {
    @Query("SELECT * FROM EachNotification ORDER BY time DESC")
    abstract fun getAllNotifications() : DataSource.Factory<Int, EachNotification>

    @Insert
    abstract fun insertNotification(eachNotification: EachNotification)
}