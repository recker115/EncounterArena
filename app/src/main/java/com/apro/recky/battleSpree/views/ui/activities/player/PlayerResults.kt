package com.apro.recky.battleSpree.views.ui.activities.player

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.adapter.RvPlayerResultAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_player_results.*
import kotlinx.android.synthetic.main.wallets_collapsing.*

class PlayerResults : AppCompatActivity() {

    private var realtimeDb : DatabaseReference?= null
    var userList : MutableList<User> = mutableListOf()
    var rvPlayerResutlAdapter : RvPlayerResultAdapter ?= null
    var rvWinnersAdapter : RvPlayerResultAdapter ?= null

    var winnerList : MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_results)

        initMembers()
    }

    fun initMembers(){

        title = "Results"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val id = intent?.extras?.getString(Constants.ID)
        realtimeDb =  AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.RESULTS)

        rvPlayerResutlAdapter = RvPlayerResultAdapter(userList, this@PlayerResults)
        rvAllResults.adapter = rvPlayerResutlAdapter
        rvAllResults.layoutManager = LinearLayoutManager(this@PlayerResults)

        rvWinnersAdapter = RvPlayerResultAdapter(winnerList, this@PlayerResults)
        rvWinners.adapter = rvPlayerResutlAdapter
        rvWinners.layoutManager = LinearLayoutManager(this@PlayerResults)

        listenToUsersJoined(id.toString())
    }

    private fun listenToUsersJoined(id : String) {

        Log.e("tourny_id", id)
        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.RESULTS)
            ?.child(id)
            ?.child(Constants.USERS_JOINED)
            ?.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java) as User
                    if (user.kills.toInt() > 0) {
                        cvWinners.visibility = View.VISIBLE
                        winnerList.add(user)
                        userList.remove(user)
                        val holderList : MutableList<User> = mutableListOf()
                        holderList.addAll(userList)
                        userList.clear()
                        userList.addAll(winnerList)
                        userList.addAll(holderList)

                        rvWinnersAdapter?.notifyDataSetChanged()
                        rvPlayerResutlAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java) as User

                    userList.add(user)

                    if (user.kills.toInt() > 0) {
                        cvWinners.visibility = View.VISIBLE
                        winnerList.add(user)
                        userList.remove(user)
                        val holderList : MutableList<User> = mutableListOf()
                        holderList.addAll(userList)
                        userList.clear()
                        userList.addAll(winnerList)
                        userList.addAll(holderList)

                        rvWinnersAdapter?.notifyDataSetChanged()
                        rvPlayerResutlAdapter?.notifyDataSetChanged()
                    }

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
