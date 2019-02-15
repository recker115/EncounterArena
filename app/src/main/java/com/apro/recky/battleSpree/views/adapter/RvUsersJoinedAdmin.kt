package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.viewHolders.UserAdminViewHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RvUsersJoinedAdmin(var usersJoined : MutableList<User>,
                         var context: Context, var tournyId : String) : RecyclerView.Adapter<UserAdminViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdminViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.user_result, parent, false)
        return UserAdminViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return usersJoined.size
    }

    override fun onBindViewHolder(holder: UserAdminViewHolder, position: Int) {
        holder.tvPubgId.text = usersJoined[position].pubgId
        holder.etKills.text = usersJoined[position].kills
        holder.etTotalAmt.text = usersJoined[position].amtWon

        holder.btnOk.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

            var counter = 1
            if (!holder.etKills.text.toString().isEmpty() && !holder.etTotalAmt.text.toString().isEmpty()) {
                holder.btnOk.isEnabled = false
                AppClass.getAppInstance()?.getRealTimeDatabase()
                    ?.child(Constants.RESULTS)
                    ?.child(tournyId)
                    ?.child(Constants.USERS_JOINED)
                    ?.child(usersJoined[position].id)
                    ?.child(Constants.KILLS)
                    ?.setValue(holder.etKills.text.toString())

                AppClass.getAppInstance()?.getRealTimeDatabase()
                    ?.child(Constants.RESULTS)
                    ?.child(tournyId)
                    ?.child(Constants.USERS_JOINED)
                    ?.child(usersJoined[position].id)
                    ?.child(Constants.MONEY_WON)
                    ?.setValue(holder.etTotalAmt.text.toString())
                    ?.addOnSuccessListener {
                        AppClass.getAppInstance()?.getRealTimeDatabase()
                            ?.child(Constants.USERS)
                            ?.child(usersJoined[position].id)
                            ?.child(Constants.WALLET_AMOUNT)
                            ?.addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    if (counter == 1) {
                                        val userAmount = p0.value.toString()
                                        val totalAmount = holder.etTotalAmt.text.toString().toDouble() + userAmount.toDouble()

                                        AppClass.getAppInstance()?.getRealTimeDatabase()
                                            ?.child(Constants.USERS)
                                            ?.child(usersJoined[position].id)
                                            ?.child(Constants.WALLET_AMOUNT)
                                            ?.setValue(totalAmount)
                                            ?.addOnSuccessListener {
                                                Utils.displayLongToast("Wallet updated successfully !", context)

                                                var adminTransactionMap : LinkedHashMap<String, String> = linkedMapOf()
                                                adminTransactionMap[Constants.IS_WITHDRAW] = "false"
                                                adminTransactionMap[Constants.TIMESTAMP] = System.currentTimeMillis().toString()
                                                adminTransactionMap[Constants.AMOUNT] = holder.etTotalAmt.text.toString()
                                                adminTransactionMap[Constants.DEPOSITED_BY] = "admin"


                                                AppClass.getAppInstance()?.getRealTimeDatabase()
                                                    ?.child(Constants.TRANSACTIONS)
                                                    ?.child(usersJoined[position].id)
                                                    ?.push()
                                                    ?.setValue(adminTransactionMap)
                                            }
                                        counter +=1
                                    }
                                }

                            })
                    }

            }

        }
    }

}