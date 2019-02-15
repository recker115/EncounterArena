package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.Transaction
import com.apro.recky.battleSpree.views.viewHolders.TransactionViewHolder
import okhttp3.internal.Util

class RvTransactionsAdapter(var transactions : MutableList<Transaction>, var context: Context) : RecyclerView.Adapter<TransactionViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        if (transaction.isWithdraw.toBoolean()) {
            var withDraw = "Withdraw Requested by ${transaction.depositedBy}"
            holder.tvTransactionBy.text = withDraw
            holder.ivType.setImageResource(R.mipmap.with_draw)
        } else {
            var deposited = "Deposited by ${transaction.depositedBy}"
            holder.tvTransactionBy.text = deposited
            holder.ivType.setImageResource(R.mipmap.deposited)
        }

        var amountStr = context.getString(R.string.rs) + transaction.amount
        holder.tvAmount.text = amountStr
        holder.tvDate.text = Utils.getAppDate(transaction.timeStamp)
    }

}