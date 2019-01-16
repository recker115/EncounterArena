package com.example.shantunu.encounterarena.views.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_tournament_details.*

class TournamentDetails : AppCompatActivity() {

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        initMembers()
    }

    private fun initMembers() {
        val id = intent?.extras?.getString(Constants.ID)

        id?.let {
            addValueEventListener(it)
        }
    }

    private fun addValueEventListener(id: String) {
        AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(id)
            ?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
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
                    tvWin.text = getString(R.string.rs) + " "+winPrize
                    tvPerkill.text = getString(R.string.rs) + " "+ perKill
                    tvDateTime.text = strTime

                    if (!roomId.equals("null", true)) {
                        if (counter == 0) {
                            tvRoomId.text = roomId
                            tvRoomPassword.text = password
                        }
                        else {
                            showRoomDialog()
                        }
                    }

                    counter += 1
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    private fun showRoomDialog() {

    }
}
