package com.example.shantunu.encounterarena.views.viewHolders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shantunu.encounterarena.R

class TournyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTournyName : TextView = itemView.findViewById(R.id.tvTournyName)
    var tvwinPrize: TextView = itemView.findViewById(R.id.tvWin)
    var tvperKill: TextView = itemView.findViewById(R.id.tvPerKill)
    var tventryFee: TextView = itemView.findViewById(R.id.tvEntryFee)
    var tvtype: TextView = itemView.findViewById(R.id.tvType)
    var tvVersion: TextView = itemView.findViewById(R.id.tvVersion)
    var pbPlayersJoined : ProgressBar = itemView.findViewById(R.id.pbPlayersFilled)
    var tvTimeStamp : TextView = itemView.findViewById(R.id.tvDateTime)
    var tvMap : TextView = itemView.findViewById(R.id.tvMap)
    var tvPlayersJoined : TextView = itemView.findViewById(R.id.tvPlayersFilled)
    var btnAddRoom : Button = itemView.findViewById(R.id.btnAddRoom)
    var ivYoutubeLink : ImageView = itemView.findViewById(R.id.ivYouLink)
    var ivYoutube : ImageView = itemView.findViewById(R.id.ivYoutube)
}