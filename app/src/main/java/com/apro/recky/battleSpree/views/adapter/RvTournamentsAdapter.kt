package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.Tournament
import com.apro.recky.battleSpree.views.ui.activities.admin.UpdateResults
import com.apro.recky.battleSpree.views.viewHolders.PlaceHolder
import com.apro.recky.battleSpree.views.viewHolders.TournyViewHolder
import com.google.android.material.textfield.TextInputEditText

class RvTournamentsAdapter(val context: Context, val tournaments : MutableList<Tournament>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_PLACEHOLDER = 1
    val TYPE_ITEM = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM) {
            val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.tourny_item, parent, false)
            TournyViewHolder(layoutView)
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

        if (holder is TournyViewHolder) {
            holder.tvTournyName.text = tournament.name
            holder.tvwinPrize.text = context.getString(R.string.rs) +" "+ tournament.winPrice
            holder.tvperKill.text = context.getString(R.string.rs) + " "+tournament.perKill
            holder.tventryFee.text = context.getString(R.string.rs) + " "+tournament.entryFee
            holder.tvtype.text = tournament.type
            holder.tvVersion.text = tournament.version
            holder.tvMap.text = tournament.map

            tournament.maxPlayers?.toInt()?.let { holder.pbPlayersJoined.max = it }

            holder.pbPlayersJoined.progress = tournament.playersJoined.toInt()
            holder.tvPlayersJoined.text = tournament.playersJoined.toString() + " / " + tournament.maxPlayers + " have joined"

            tournament.timeStamp?.let { holder.tvTimeStamp.text = Utils.getAppDate(it) }

            if (tournament.isRoomCreated.equals("true", true)) {
                holder.btnAddRoom.visibility = View.GONE
            }
            else {
                holder.btnAddRoom.visibility = View.VISIBLE
            }


            holder.btnAddRoom.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                val dialogCreateRoom = Utils.getDialog(context, R.layout.dialog_add_room)
                val etRoomId = dialogCreateRoom.findViewById<View>(R.id.etRoomId) as TextInputEditText
                val etPassword = dialogCreateRoom.findViewById<View>(R.id.etPassword) as TextInputEditText
                val btnCreateRoom = dialogCreateRoom.findViewById<View>(R.id.btnCreateRoom)
                dialogCreateRoom.show()

                btnCreateRoom.setOnClickListener {
                    when {
                        etRoomId.text.toString().isEmpty() -> etRoomId.error = "Fill it"
                        etPassword.text.toString().isEmpty() -> etPassword.error = "Fill it"
                        else-> {
                            dialogCreateRoom.dismiss()

                            AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)
                                ?.child(Constants.ROOM_ID)?.setValue(etRoomId.text.toString())
                            AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)
                                ?.child(Constants.PASSWORD)?.setValue(etPassword.text.toString())
                            AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)
                                ?.child(Constants.IS_ROOM_CREATED)?.setValue("true")
                            notifyDataSetChanged()

                        }
                    }
                }
            }

            holder.vRoot.setOnClickListener{
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                val intent = Intent(context, UpdateResults::class.java)
                intent.putExtra(Constants.ID, tournament.id)
                context.startActivity(intent)
            }

            if (tournament.roomId.isEmpty()){
                holder.ivAdminOngoing.visibility = View.GONE
            }
            else {
                holder.ivAdminOngoing.visibility = View.VISIBLE
            }

            holder.ivAdminOngoing.setOnClickListener{
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)
                    ?.child(Constants.IS_ONGOING)?.setValue(true)
            }

            if (tournament.youtubeLink.isEmpty()){
                holder.ivYoutubeLink.visibility = View.VISIBLE
                holder.ivYoutube.visibility = View.GONE
            }
            else {
                holder.ivYoutubeLink.visibility = View.GONE
                holder.ivYoutube.visibility = View.VISIBLE
            }

            holder.ivYoutubeLink.setOnClickListener{
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                val dialogSetYoutubeLink = Utils.getDialog(context, R.layout.dialog_add_link)
                val etLink = dialogSetYoutubeLink.findViewById<View>(R.id.etLink) as TextInputEditText

                val btnSetLink = dialogSetYoutubeLink.findViewById<View>(R.id.btnAddLink)
                dialogSetYoutubeLink.show()

                btnSetLink.setOnClickListener{ buttons->
                    buttons.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                    when {
                        etLink.text.toString().isEmpty() -> etLink.error = "Fill it"

                        else-> {
                            dialogSetYoutubeLink.dismiss()
                            tournament.id.let { it1 ->
                                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(it1)
                                    ?.child(Constants.YOUTUBE_LINK)?.setValue(etLink.text.toString())
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (tournaments[position].name == null)
            TYPE_PLACEHOLDER
        else
            TYPE_ITEM
    }

}