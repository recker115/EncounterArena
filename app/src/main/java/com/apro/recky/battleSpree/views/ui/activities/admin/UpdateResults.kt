package com.apro.recky.battleSpree.views.ui.activities.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.adapter.RvUsersJoinedAdmin
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_update_results.*

class UpdateResults : AppCompatActivity() {
    var realtimeDb : DatabaseReference?= null
    var userList : MutableList<User> = mutableListOf()
    var rvUsersAdapter : RvUsersJoinedAdmin ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_results)

        initMembers()
    }

    fun initMembers(){
        val id = intent?.extras?.getString(Constants.ID)
        realtimeDb =  AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.RESULTS)

        rvUsersAdapter = RvUsersJoinedAdmin(userList, this@UpdateResults, id.toString())
        rvUsersJoined.adapter = rvUsersAdapter
        rvUsersJoined.layoutManager = LinearLayoutManager(this)

        listenToUsersJoined(id.toString())
    }

    private fun listenToUsersJoined(id : String) {

        Log.e("tourny_id", id)
        AppClass.getAppInstance()?.getRealTimeDatabase()
            ?.child(Constants.RESULTS)
            ?.child(id)
            ?.child(Constants.USERS_JOINED)
            ?.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var user = p0.getValue(User::class.java) as User

                    userList.add(user)
                    rvUsersAdapter?.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
    }

}
