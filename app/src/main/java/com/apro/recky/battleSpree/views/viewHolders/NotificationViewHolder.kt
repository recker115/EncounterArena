package com.apro.recky.battleSpree.views.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
    var tvRoomId : TextView = itemView.findViewById(R.id.tvRoomId)
    var tvRoomPass : TextView = itemView.findViewById(R.id.tvRoomPass)
}