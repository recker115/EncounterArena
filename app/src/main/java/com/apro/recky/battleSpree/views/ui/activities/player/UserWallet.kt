package com.apro.recky.battleSpree.views.ui.activities.player

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user_wallet.*
import kotlinx.android.synthetic.main.wallets_collapsing.*

class UserWallet : AppCompatActivity() {
    var currentUuId = ""
    var balance = ""
    var userWalletAmt = 0.0
    var email = ""
    var isWithDrawProcessing = false
    var phnNumber = ""
    var fcmToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet)

        initMembers()
    }

    private fun initMembers() {

        collapsingToolbar.isTitleEnabled = false
        title = "My Wallet"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        btnAddMoney.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            startActivity(Intent(this@UserWallet, AddMoney::class.java))
        }

        tvTransactions.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            startActivity(Intent(this@UserWallet, TransactionsActivity::class.java))
        }

        btnWithdraw.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

            if (email.isEmpty()) {
                Utils.displayLongToast("Please connect to internet !", this@UserWallet)
            } else {
                if(!etWithdrawAmt.text.toString().isEmpty()){
                    if (etWithdrawAmt.text.toString().toDouble() > 10)
                        updateWithDraw()
                    else
                        Utils.displayLongToast("Minimum amount should be 10", this@UserWallet)
                }
                else {
                    Utils.displayLongToast("Enter the amount to be withdrawn !", this@UserWallet)
                    etWithdrawAmt.requestFocus()
                }
            }

        }

        listenToUserDb()
    }

    private fun updateWithDraw() {
        if (!isWithDrawProcessing) {
            if (etWithdrawAmt.text.toString().toDouble() < userWalletAmt) {
                Utils.displayLongToast("You don't have enough amount in wallet to withdraw", this@UserWallet)
            } else {
                updateUserTransactions(email)
                updateAdminTransactions(email)
                etWithdrawAmt.setText("")
            }
        } else {
            Utils.displayLongToast("One withdraw request is already being processed!", this@UserWallet)
        }
    }

    private fun updateAdminTransactions(email : String) {

        val key = AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.WITHDRAW_REQUEST)
            ?.child(currentUuId)
            ?.push()?.key

        val transactionsMap = linkedMapOf<String, String>()
        transactionsMap[Constants.AMOUNT] = etWithdrawAmt.text.toString()
        transactionsMap[Constants.TIMESTAMP] = System.currentTimeMillis().toString()
        transactionsMap[Constants.REQUESTED_BY] = email
        transactionsMap[Constants.PHONE_NUMBER] = phnNumber
        transactionsMap[Constants.FCM_TOKEN] = fcmToken
        transactionsMap[Constants.IS_PAID] = "false"
        transactionsMap[Constants.ID] = key.toString()

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.WITHDRAW_REQUEST)
            ?.child(key.toString())
            ?.setValue(transactionsMap)
            ?.addOnSuccessListener {
            }
    }

    private fun updateUserTransactions(email : String) {

        val transactionsMap = linkedMapOf<String, String>()
        transactionsMap[Constants.IS_WITHDRAW] = "true"
        transactionsMap[Constants.AMOUNT] = etWithdrawAmt.text.toString()
        transactionsMap[Constants.TIMESTAMP] = System.currentTimeMillis().toString()
        transactionsMap[Constants.REQUESTED_BY] = email

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.TRANSACTIONS)
            ?.child(currentUuId)
            ?.push()
            ?.setValue(transactionsMap)

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.USERS)
            ?.child(currentUuId)
            ?.child(Constants.AMOUNT_REQUESTED)
            ?.setValue(etWithdrawAmt.text.toString())

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.USERS)
            ?.child(currentUuId)
            ?.child(Constants.IS_WITHDRAW_PROCESSING)
            ?.setValue("true")

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.USERS)
            ?.child(currentUuId)
            ?.child(Constants.WALLET_AMOUNT)
            ?.setValue((balance.toDouble() - etWithdrawAmt.text.toString().toDouble()).toString())

    }

    private fun listenToUserDb() {

        currentUuId = AppClass.getAppInstance()?.currentUuId.toString()

        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.USERS)
            ?.child(currentUuId)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    balance = p0.child(Constants.WALLET_AMOUNT).value.toString()
                    val number = p0.child(Constants.PHONE_NUMBER).value.toString()
                    userWalletAmt = p0.child(Constants.WALLET_AMOUNT).value.toString().toDouble()
                    email = p0.child(Constants.EMAIL).value.toString()
                    isWithDrawProcessing = p0.child(Constants.IS_WITHDRAW_PROCESSING).value.toString().toBoolean()
                    val amountProcessing = p0.child(Constants.AMOUNT_REQUESTED).value.toString()
                    fcmToken = p0.child(Constants.FCM_TOKEN).value.toString()
                    phnNumber = p0.child(Constants.PHONE_NUMBER).value.toString()

                    if (amountProcessing != "null") {
                        if (amountProcessing.toDouble() > 0)
                            tvAmtProcessing.text = "Withdraw processing "+ getString(R.string.rs) + amountProcessing
                        else {
                            tvAmtProcessing.text = ""
                        }
                    } else {
                        tvAmtProcessing.text = ""
                    }

                    tvBalance.text = getString(R.string.rs)+" "+balance
                    tvNumber.text = number

                }

            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
