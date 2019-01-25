package com.apro.recky.battleSpree.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user_wallet.*
import kotlinx.android.synthetic.main.wallets_collapsing.*

class UserWallet : AppCompatActivity() {

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

        listenToUserDb()
    }

    private fun listenToUserDb() {

        var currentUuId = AppClass.getAppInstance()?.currentUuId

        if (currentUuId != null) {
            AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
                ?.child(currentUuId)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var balance = p0.child(Constants.WALLET_AMOUNT).value.toString()
                        var number = p0.child(Constants.PHONE_NUMBER).value.toString()

                        tvBalance.text = getString(R.string.rs)+" "+balance
                        tvNumber.text = number
                    }

                })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
