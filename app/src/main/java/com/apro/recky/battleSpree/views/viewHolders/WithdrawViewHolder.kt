package com.apro.recky.battleSpree.views.viewHolders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R

class WithdrawViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var tvEmail = itemView.findViewById<View>(R.id.tvEmail) as TextView
    var tvAmount = itemView.findViewById<View>(R.id.tvAmt)as TextView
    var tvPhnNumber = itemView.findViewById<View>(R.id.tvPhoneNumber)as TextView
    var btnPay = itemView.findViewById<View>(R.id.btnPay)as Button
    var ivPaidLabel = itemView.findViewById<View>(R.id.ivPaidLabel)as ImageView
    var tvPaidLabel = itemView.findViewById<View>(R.id.tvPaidLabel)as TextView
}