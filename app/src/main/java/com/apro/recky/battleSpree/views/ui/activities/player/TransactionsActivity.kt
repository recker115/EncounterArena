package com.apro.recky.battleSpree.views.ui.activities.player

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.Transaction
import com.apro.recky.battleSpree.views.adapter.RvTransactionsAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.wallets_collapsing.*

class TransactionsActivity : AppCompatActivity() {

    var currentUuid = ""
    var transactions : MutableList<Transaction> = mutableListOf()
    var rvTransactionsAdapter : RvTransactionsAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        initMembers()
    }

    private fun initMembers() {
        title = "Transactions"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        currentUuid = AppClass.getAppInstance()?.currentUuId.toString()

        rvTransactionsAdapter = RvTransactionsAdapter(transactions, this@TransactionsActivity)
        rvTransactions.adapter = rvTransactionsAdapter
        rvTransactions.layoutManager = LinearLayoutManager(this@TransactionsActivity)
        listenToRealtime()
    }

    private fun listenToRealtime() {
        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.TRANSACTIONS)
            ?.child(currentUuid)
            ?.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val transaction = p0.getValue(Transaction::class.java) as Transaction
                    transactions.add(transaction)
                    rvTransactionsAdapter?.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
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
