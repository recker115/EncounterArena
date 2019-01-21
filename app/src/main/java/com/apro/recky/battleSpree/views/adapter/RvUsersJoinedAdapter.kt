package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.viewHolders.UserViewHolder

class RvUsersJoinedAdapter(val usersJoined : MutableList<User>, context : Context) : RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return UserViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return usersJoined.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvPubgId.text = usersJoined[position].pubgId
    }
}