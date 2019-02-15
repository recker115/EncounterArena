package com.apro.recky.battleSpree.views.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R

class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTransactionBy = itemView.findViewById<View>(R.id.tvDepositedBy) as TextView
    var tvAmount = itemView.findViewById<View>(R.id.tvAmount) as TextView
    var ivType = itemView.findViewById<View>(R.id.ivType) as ImageView
    var tvDate = itemView.findViewById<View>(R.id.tvDate) as TextView
}