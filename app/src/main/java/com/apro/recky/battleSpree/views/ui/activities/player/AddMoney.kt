package com.apro.recky.battleSpree.views.ui.activities.player

import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import instamojo.library.InstamojoPay
import instamojo.library.InstapayListener
import kotlinx.android.synthetic.main.activity_add_money.*
import kotlinx.android.synthetic.main.wallets_collapsing.*
import org.json.JSONObject
import java.lang.Exception

class AddMoney : AppCompatActivity() {

    var instamojoListener : InstapayListener ?= null
    var currentUuId = ""
    var isClicked = false
    var currentAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money)

        initMembers()
    }

    private fun initMembers() {
        collapsingToolbar.isTitleEnabled = false
        title = "Add Money"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        currentUuId = AppClass.getAppInstance()?.currentUuId.toString()
        etAmount.requestFocus()

        ivWallets.visibility = View.GONE

        etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    fabAddAmount.visibility = View.GONE
                } else {
                    fabAddAmount.visibility = View.VISIBLE
                }
            }

        })
        fabAddAmount.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            isClicked = true
            AppClass.getAppInstance()?.getRealTimeDatabase()
                ?.child(Constants.USERS)
                ?.child(currentUuId)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        val email = p0.child(Constants.EMAIL).value.toString()
                        val phone = p0.child(Constants.PHONE_NUMBER).value.toString()
                        val name = p0.child(Constants.FULL_NAME).value.toString()
                        currentAmount = p0.child(Constants.WALLET_AMOUNT).value.toString()

                        if (isClicked) {
                            callInstamojoPay(email = email, phone = phone, buyerName = name,
                                amount = etAmount.text.toString(), purpose = "PubG")

                        }

                        isClicked = false

                    }

                })
        }

    }

    fun callInstamojoPay(email : String, phone : String, amount : String, purpose : String, buyerName : String) {
        val activity = this@AddMoney
        val instamojoPay = InstamojoPay()
        val intentFilter = IntentFilter("ai.devsupport.instamojo")
        registerReceiver(instamojoPay,intentFilter)
        val pay = JSONObject()
        try {
            pay.put("email", email)
            pay.put("phone", phone)
            pay.put("amount", amount)
            pay.put("purpose", purpose)
            pay.put("name", buyerName)
            pay.put("send_sms", true)
            pay.put("send_email", true)
        }catch ( e : Exception) {
            e.printStackTrace()
        }
        initListener(amount)
        instamojoPay.start(activity, pay, instamojoListener)
    }

    private fun initListener(amount: String) {
        instamojoListener = object : InstapayListener{
            override fun onSuccess(p0: String?) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG)
                    .show()
                updateUserWallet(amount)
            }

            override fun onFailure(p0: Int, p1: String?) {
                Toast.makeText(applicationContext, "Failed: $p1", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun updateUserWallet(amount: String) {

        if (!currentAmount.isEmpty()) {
            AppClass.getAppInstance()?.getRealTimeDatabase()
                ?.child(Constants.USERS)
                ?.child(currentUuId)
                ?.child(Constants.WALLET_AMOUNT)?.setValue((currentAmount.toInt() + amount.toInt()))
                ?.addOnCompleteListener{
                    if (it.isSuccessful){
                        // dismiss dialog
                        onBackPressed()
                        finish()
                    }
                }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
