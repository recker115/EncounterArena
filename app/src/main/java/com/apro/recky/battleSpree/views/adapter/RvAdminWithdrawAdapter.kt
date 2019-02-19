package com.apro.recky.battleSpree.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.Withdraw
import com.apro.recky.battleSpree.views.viewHolders.WithdrawViewHolder

class RvAdminWithdrawAdapter(var withdraws : MutableList<Withdraw>, var context: Context) : RecyclerView.Adapter<WithdrawViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.withdraw_item, parent, false)
        return WithdrawViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return withdraws.size
    }

    override fun onBindViewHolder(holder: WithdrawViewHolder, position: Int) {
        val withdraw = withdraws[position]

        val amount = context.getString(R.string.rs) + withdraw.amount
        val email = withdraw.email
        val phnNumber = withdraw.phnNumber
        val isWithdrawn = withdraw.isPaid.toBoolean()

        if (isWithdrawn) {
            holder.btnPay.visibility = View.GONE
            holder.ivPaidLabel.visibility = View.VISIBLE
            holder.tvPaidLabel.visibility = View.VISIBLE
        } else {
            holder.btnPay.visibility = View.VISIBLE
            holder.ivPaidLabel.visibility = View.GONE
            holder.tvPaidLabel.visibility = View.GONE
        }

        holder.tvAmount.text = amount
        holder.tvEmail.text = email
        holder.tvPhnNumber.text = phnNumber

        holder.btnPay.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            AppClass.getAppInstance()?.getRealTimeDatabase()
                ?.child(Constants.WITHDRAW_REQUEST)
                ?.child(withdraw.id)
                ?.child(Constants.IS_WITHDRAW)
                ?.setValue("true")

            AppClass.getAppInstance()?.getRealTimeDatabase()
                ?.child(Constants.USERS)
                ?.child(AppClass.getAppInstance()?.currentUuId.toString())
                ?.child(Constants.AMOUNT_REQUESTED)
                ?.setValue("0")

            AppClass.getAppInstance()?.getRealTimeDatabase()
                ?.child(Constants.USERS)
                ?.child(AppClass.getAppInstance()?.currentUuId.toString())
                ?.child(Constants.IS_WITHDRAW_PROCESSING)
                ?.setValue("false")

        }
    }

}