package com.example.shantunu.encounterarena.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.Utils
import com.example.shantunu.encounterarena.firebaseModels.Tournament
import com.example.shantunu.encounterarena.views.viewHolders.PlaceHolder
import com.example.shantunu.encounterarena.views.viewHolders.TournyPlayerViewHolder
import com.example.shantunu.encounterarena.views.viewHolders.TournyViewHolder
import com.google.android.material.textfield.TextInputEditText

class RvPlayerTournamentAdapter(val context: Context, val tournaments : MutableList<Tournament>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_PLACEHOLDER = 1
    val TYPE_ITEM = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM) {
            val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.tourny_item_player, parent, false)
            TournyPlayerViewHolder(layoutView)
        } else {
            val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.shimmer_placeholder, parent, false)
            PlaceHolder(layoutView)
        }

    }

    override fun getItemCount(): Int {
        return tournaments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var tournament = tournaments[position]

        if (holder is TournyPlayerViewHolder) {
            holder.tvTournyName.text = tournament.name
            holder.tvwinPrize.text = context.getString(R.string.rs) +" "+ tournament.winPrice
            holder.tvperKill.text = context.getString(R.string.rs) + " "+tournament.perKill
            holder.tventryFee.text = context.getString(R.string.rs) + " "+tournament.entryFee
            holder.tvtype.text = tournament.type
            holder.tvVersion.text = tournament.version
            holder.tvMap.text = tournament.map

            tournament.maxPlayers?.toInt()?.let { holder.pbPlayersJoined.max = it }
            tournament.playersJoined?.toInt()?.let {
                holder.pbPlayersJoined.progress = it
                holder.tvPlayersJoined.text = it.toString() + " / " + tournament.maxPlayers + " have joined"
            }
            tournament.timeStamp?.let { holder.tvTimeStamp.text = Utils.getAppDate(it) }

            /*tournament.isRoomCreated?.let {
                if (it.equals("true", true)) {
                    holder.btnAddRoom.visibility = View.GONE
                }
                else {
                    holder.btnAddRoom.visibility = View.VISIBLE
                }
            }*/

            holder.btnJoin.setOnClickListener {
               // perform join
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (tournaments[position].name == null) {
            TYPE_PLACEHOLDER
        } else
            TYPE_ITEM
    }

}