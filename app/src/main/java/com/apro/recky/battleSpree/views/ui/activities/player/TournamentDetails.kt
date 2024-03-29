package com.apro.recky.battleSpree.views.ui.activities.player

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.adapter.RvUsersJoinedAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import kotlinx.android.synthetic.main.activity_tournament_details.*

class TournamentDetails : AppCompatActivity() {

    private val usersJoined: MutableList<User> = mutableListOf()
    var rvUsersJoinedAdapter : RvUsersJoinedAdapter ?= null
    var counter = 0
    var dialogRoom : Dialog ?= null
    var isOnGoing : Boolean ?= false
    var realtimeDb : DatabaseReference ?= null
    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        initMembers()
    }

    private fun initMembers() {
        val id = intent?.extras?.getString(Constants.ID)
        isOnGoing = intent?.extras?.getBoolean(Constants.IS_ONGOING)

        isOnGoing?.let {

            realtimeDb = if (it) {
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.RESULTS)
            } else {
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)
            }

        }

        rvUsersJoinedAdapter = RvUsersJoinedAdapter(usersJoined, this@TournamentDetails)
        rvUsersJoined.adapter = rvUsersJoinedAdapter
        rvUsersJoined.layoutManager = LinearLayoutManager(this@TournamentDetails, LinearLayoutManager.HORIZONTAL, false)

        ivPlay.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        id?.let {
            addValueEventListener(it)
        }
    }

    private fun addValueEventListener(id: String) {
        realtimeDb?.child(id)
            ?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {

                    if (counter == 0) {

                        var tourneyName = p0.child(Constants.TOURNY_NAME).value.toString()
                        var winPrize = p0.child(Constants.WINS_AMT).value.toString()
                        var perKill = p0.child(Constants.PER_KILL).value.toString()
                        var type = p0.child(Constants.TYPE).value.toString()
                        var version = p0.child(Constants.VERSION).value.toString()
                        var map = p0.child(Constants.MAP).value.toString()
                        var roomId = p0.child(Constants.ROOM_ID).value.toString()
                        var password = p0.child(Constants.PASSWORD).value.toString()
                        var strTime = Utils.getAppDate(p0.child(Constants.TIMESTAMP).value.toString())

                        tvTourneyName.text = tourneyName
                        tvType.text = type
                        tvVersion.text = version
                        tvMap.text = map
                        tvWin.text = getString(R.string.rs) + " " + winPrize
                        tvPerkill.text = getString(R.string.rs) + " " + perKill
                        tvDateTime.text = strTime

                        if (!roomId.equals("null", true)) {
                            tvRoomId.text = roomId
                            tvRoomPassword.text = password
                        } else {
                            tvRoomId.text = "Please wait !"
                            tvRoomPassword.text = "Please wait !"
                        }

                    } else {
                        var roomId = p0.child(Constants.ROOM_ID).value.toString()
                        var password = p0.child(Constants.PASSWORD).value.toString()

                        if (!roomId.equals("null", true)) {
                            tvRoomId.text = roomId
                            tvRoomPassword.text = password
                            showRoomDialog(roomId, password)
                        }
                    }

                    for (eachSnapshot in p0.child(Constants.USERS_JOINED).children) {
                        var user = eachSnapshot.getValue(User::class.java) as User
                        usersJoined.add(user)
                    }

                    url = p0.child(Constants.YOUTUBE_LINK).value.toString()
                    rvUsersJoinedAdapter?.notifyDataSetChanged()

                    counter += 1
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    private fun showRoomDialog(roomId : String , password : String) {

        if (dialogRoom != null) {
            dialogRoom?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }
        }
        else {
            dialogRoom = Utils.getDialog(this@TournamentDetails, R.layout.dialog_room)
            var tvRoomIDDialog = dialogRoom?.findViewById<View>(R.id.tvRoomIdDialog) as TextView
            var tvRoomPassword = dialogRoom?.findViewById<View>(R.id.tvPasswordDialog) as TextView
            var progressBar = dialogRoom?.findViewById<View>(R.id.pbMain) as ProgressBar
            var loadingView = dialogRoom?.findViewById<View>(R.id.loadingView)
            var btnOk = dialogRoom?.findViewById<View>(R.id.btnOk) as Button

            btnOk.setOnClickListener {
                dialogRoom?.dismiss()
            }

            tvRoomIDDialog.text = roomId
            tvRoomPassword.text = password

            loadingView?.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            progressBar.indeterminateDrawable = FoldingCirclesDrawable.Builder(this@TournamentDetails).build()

            Handler().postDelayed({
                loadingView?.visibility = View.GONE
                progressBar.visibility = View.GONE
            },2000)

            dialogRoom?.show()

        }
    }
}
