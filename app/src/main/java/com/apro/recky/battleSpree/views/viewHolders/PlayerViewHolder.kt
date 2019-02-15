package com.apro.recky.battleSpree.views.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var tvpubgId = itemView.findViewById<View>(R.id.tvPubgId) as TextView
    var tvKills = itemView.findViewById<View>(R.id.tvKills) as TextView
    var tvTotalWon = itemView.findViewById<View>(R.id.tvTotalWon)as TextView
}