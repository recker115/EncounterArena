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

class RvPlayerTournamentAdapter(val context: Context, val tournaments : MutableList<Tournament>,
                                val curUserId : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            holder.pbPlayersJoined.progress = tournament.playersJoined.toInt()
            holder.tvPlayersJoined.text = tournament.playersJoined + " / " + tournament.maxPlayers + " have joined"

            tournament.timeStamp?.let { holder.tvTimeStamp.text = Utils.getAppDate(it) }

            if (tournament.isCurrentUserJoined)
                holder.btnJoin.visibility = View.GONE
            else
                holder.btnJoin.visibility = View.VISIBLE

            holder.btnJoin.setOnClickListener {
               // perform join
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)
                    ?.child(tournament.id)?.child(Constants.PLAYERS_JOINED)?.setValue((tournament.playersJoined.toInt()+1).toString())

                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)
                    ?.child(Constants.USERS_JOINED)
                    ?.push()?.child(Constants.ID)?.setValue(curUserId)
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