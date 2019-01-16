package com.example.shantunu.encounterarena.views.viewHolders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shantunu.encounterarena.R

class TournyPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    var btnJoin : Button = itemView.findViewById(R.id.btnJoin)
    var tvJoined : TextView = itemView.findViewById(R.id.tvJoined)
    var ivJoined : ImageView = itemView.findViewById(R.id.ivJoined)
    var vRoot : View = itemView.findViewById(R.id.vRoot)
}