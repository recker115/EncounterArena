package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.Tournament
import com.apro.recky.battleSpree.views.ui.activities.admin.UpdateResults
import com.apro.recky.battleSpree.views.ui.activities.player.PlayerResults
import com.apro.recky.battleSpree.views.ui.activities.player.TournamentDetails
import com.apro.recky.battleSpree.views.viewHolders.PlaceHolder
import com.apro.recky.battleSpree.views.viewHolders.TournyPlayerViewHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import java.util.LinkedHashMap

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

            if (tournament.isCurrentUserJoined) {
                holder.btnJoin.visibility = View.GONE
                holder.ivJoined.visibility = View.VISIBLE
                holder.tvJoined.visibility = View.VISIBLE

            }
            else {
                if (tournament.isOngoing) {
                    holder.btnJoin.visibility = View.GONE
                } else {
                    holder.btnJoin.visibility = View.VISIBLE
                }
                holder.ivJoined.visibility = View.GONE
                holder.tvJoined.visibility = View.GONE
            }

            holder.btnJoin.setOnClickListener {
               // perform join
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
                showAddPUBGIDdialog(tournament)
            }

            holder.vRoot.setOnClickListener{
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

                if (!tournament.isOngoing) {
                    if (tournament.isCurrentUserJoined) {
                        var intent = Intent(context, TournamentDetails::class.java)
                        intent.putExtra(Constants.ID, tournament.id)
                        intent.putExtra(Constants.IS_ONGOING, tournament.isOngoing)
                        context.startActivity(intent)
                    }
                } else {
                    val intent = Intent(context, PlayerResults::class.java)
                    intent.putExtra(Constants.ID, tournament.id)
                    context.startActivity(intent)
                }

            }
        }

    }

    private fun showAddPUBGIDdialog(tournament : Tournament) {
        val dialogAddId = Utils.getDialog(context, R.layout.dialog_pubg_id)
        val etPubGid = dialogAddId.findViewById<View>(R.id.etPubGId) as TextInputEditText
        val btnSetId = dialogAddId.findViewById<View>(R.id.btnAddPubgId) as Button
        val tvInitialAmt = dialogAddId.findViewById<View>(R.id.tvEarlierAmt) as TextView
        val tvFinalAmt = dialogAddId.findViewById<View>(R.id.tvFinal) as TextView
        var initialAmt = 0.0

        tvInitialAmt.paintFlags = tvInitialAmt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        btnSetId.isEnabled = false

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.USERS)
            ?.child(curUserId)
            ?.child(Constants.WALLET_AMOUNT)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    initialAmt = p0.value.toString().toDouble()
                    tvInitialAmt.text = context.getString(R.string.rs) + " "+initialAmt.toInt()
                    if (tournament.entryFee.toDouble() < initialAmt) {
                        tvFinalAmt.text = context.getString(R.string.rs)+ " "+(initialAmt.toInt() - tournament.entryFee.toInt()).toString()
                        btnSetId.isEnabled = true
                    } else {
                        tvFinalAmt.text = "Entry fee is "+ context.getString(R.string.rs) +" "+ tournament.entryFee
                        tvInitialAmt.paintFlags = tvInitialAmt.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        Toast.makeText(context, "You dont have enough balance to register", Toast.LENGTH_LONG).show()
                    }

                }
            })
        btnSetId.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            if (etPubGid.text.toString().isEmpty()) {
                etPubGid.error = "Fill it to join"
            }
            else {
                dialogAddId.dismiss()
                AppClass.getAppInstance()?.getRealTimeDatabase()
                    ?.child(Constants.USERS)
                    ?.child(curUserId)
                    ?.child(Constants.WALLET_AMOUNT)
                    ?.setValue((initialAmt - tournament.entryFee.toDouble()).toString())
                    ?.addOnSuccessListener {
                        if (tournament.playersJoined.toInt() <= tournament.maxPlayers?.toInt()!!) {
                            AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)
                                ?.child(tournament.id)?.child(Constants.PLAYERS_JOINED)?.setValue((tournament.playersJoined.toInt()+1).toString())

                            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { token ->
                                run {
                                    val strToken = token.token

                                    var userJoinedMap : LinkedHashMap<String, String> = LinkedHashMap()
                                    userJoinedMap[Constants.ID] = curUserId
                                    userJoinedMap[Constants.PUBG_ID] = etPubGid.text.toString()
                                    userJoinedMap[Constants.FCM_TOKEN] = strToken
                                    userJoinedMap[Constants.KILLS] = "0"
                                    userJoinedMap[Constants.MONEY_WON] = "0"

                                    AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)
                                        ?.child(tournament.id)
                                        ?.child(Constants.USERS_JOINED)
                                        ?.child(curUserId)?.setValue(userJoinedMap)
                                }
                            }
                        }
                    }
            }
        }

        dialogAddId.show()

    }

    override fun getItemViewType(position: Int): Int {
        return if (tournaments[position].name == null) {
            TYPE_PLACEHOLDER
        } else
            TYPE_ITEM
    }

}