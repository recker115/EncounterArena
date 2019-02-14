package com.apro.recky.battleSpree.views.ui.activities.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.Tournament
import com.apro.recky.battleSpree.views.adapter.RvTournamentsAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

    var tournaments : MutableList<Tournament> = mutableListOf()
    var rvTournyAdapter : RvTournamentsAdapter?= null
    var counterFirst = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        initMembers()
    }

    private fun initMembers() {
        title = "Results"
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tournaments.add(Tournament())
        tournaments.add(Tournament())

        rvTournyAdapter = RvTournamentsAdapter(this, tournaments)
        rvResults.adapter = rvTournyAdapter
        rvResults.layoutManager = LinearLayoutManager(this)
        listenTournamentDatabase()
    }

    private fun listenTournamentDatabase() {
        AppClass.APPINSTANCE?.getRealTimeDatabase()?.child(Constants.RESULTS)?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (counterFirst == 0) {
                    tournaments.clear()
                }

                counterFirst +=1

                var tournament = p0.getValue(Tournament::class.java) as Tournament
                tournaments.add(tournament)
                rvTournyAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var tournament = p0.getValue(Tournament::class.java) as Tournament

                for (currTournament in tournaments) {
                    if (tournament.id == currTournament.id) {
                        tournament = currTournament
                    }
                }

                tournaments.remove(tournament)
                rvTournyAdapter?.notifyDataSetChanged()
            }

        })
    }
}
