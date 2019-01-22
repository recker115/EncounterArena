package com.apro.recky.battleSpree.views.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.apro.recky.battleSpree.R
import kotlinx.android.synthetic.main.activity_add_money.*
import kotlinx.android.synthetic.main.wallets_collapsing.*

class AddMoney : AppCompatActivity() {

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

        etAmount.requestFocus()

        ivWallets.visibility = View.GONE
//        showKeyboard()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
