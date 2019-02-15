package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.viewHolders.PlayerViewHolder

class RvPlayerResultAdapter(var playerList : MutableList<User>, var context: Context) : RecyclerView.Adapter<PlayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.player_item_result, parent, false)
        return PlayerViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        var user = playerList[position]
        holder.tvpubgId.text = user.pubgId

        val kills = "Kills - ${user.kills}"
        holder.tvKills.text = kills

        val moneyWon = context.getString(R.string.rs) + " "+user.amtWon
        holder.tvTotalWon.text = moneyWon
    }
}