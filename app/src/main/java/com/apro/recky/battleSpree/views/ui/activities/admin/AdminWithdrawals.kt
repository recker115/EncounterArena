package com.apro.recky.battleSpree.views.ui.activities.admin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.Withdraw
import com.apro.recky.battleSpree.views.adapter.RvAdminWithdrawAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_admin_withdrawals.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*

class AdminWithdrawals : AppCompatActivity() {

    val withdrawals : MutableList<Withdraw> = mutableListOf()
    var rvAdminWithdrawAdapter : RvAdminWithdrawAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_withdrawals)

        initMembers()
        listenToRealtime()
    }

    private fun listenToRealtime() {
        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.WITHDRAW_REQUEST)
            ?.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val withdraw = p0.getValue(Withdraw::class.java) as Withdraw

                    for (eachWithdraw in withdrawals) {
                        if (eachWithdraw.id == withdraw.id) {
                            eachWithdraw.isPaid = "true"
                        }
                    }

                    rvAdminWithdrawAdapter?.notifyDataSetChanged()
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val withdraw = p0.getValue(Withdraw::class.java) as Withdraw

                    withdrawals.add(withdraw)
                    rvAdminWithdrawAdapter?.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }

            })
    }

    private fun initMembers() {
        title = "Withdraw Requests"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvAdminWithdrawAdapter = RvAdminWithdrawAdapter(withdrawals, this@AdminWithdrawals)
        rvWithdrawals.adapter = rvAdminWithdrawAdapter
        val layoutManager = LinearLayoutManager(this@AdminWithdrawals)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        rvWithdrawals.layoutManager = layoutManager

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
