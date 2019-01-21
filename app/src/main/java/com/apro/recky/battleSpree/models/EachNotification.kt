package com.apro.recky.battleSpree.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EachNotification(
    @ColumnInfo(name = "Title") var title : String?,
    @ColumnInfo(name = "Type") var type : String?,
    @ColumnInfo(name = "RoomId") var roomId : String?,
    @ColumnInfo(name = "Password") var password : String?,
    @ColumnInfo(name = "Time") var time : Long
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}