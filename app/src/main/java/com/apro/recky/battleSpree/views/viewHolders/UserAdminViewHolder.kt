package com.apro.recky.battleSpree.views.viewHolders

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R

class UserAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvPubgId : TextView = itemView.findViewById(R.id.tvPlayerName) as TextView
    var etKills : TextView = itemView.findViewById(R.id.etKills) as EditText
    var etTotalAmt : TextView = itemView.findViewById(R.id.etTotalWon) as EditText
    var btnOk : TextView = itemView.findViewById(R.id.btnOk) as Button
}