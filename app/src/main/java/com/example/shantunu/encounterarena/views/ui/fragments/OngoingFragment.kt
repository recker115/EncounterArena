package com.example.shantunu.encounterarena.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.Utils
import com.example.shantunu.encounterarena.models.Tournament
import com.example.shantunu.encounterarena.views.adapter.RvPlayerTournamentAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_ongoing.*

class OngoingFragment : Fragment() {

    var counterFirst = 0
    var ongoingTournaments = mutableListOf<Tournament>()
    var rvTournamentPlayerAdapter : RvPlayerTournamentAdapter?= null
    var currUserID : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ongoing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
    }

    private fun initMembers() {

        ongoingTournaments.add(Tournament())
        ongoingTournaments.add(Tournament())
        ongoingTournaments.add(Tournament())

        Utils.getCurrentUser()?.uid ?.let {
            currUserID = it
        }

        activity?.let {
            rvTournamentPlayerAdapter = RvPlayerTournamentAdapter(it, ongoingTournaments, currUserID)
            rvOngoingTournaments.adapter = rvTournamentPlayerAdapter
            rvOngoingTournaments.layoutManager = LinearLayoutManager(it)
        }

        setChildListeners()
    }

    fun setChildListeners() {

        AppClass.APPINSTANCE?.getRealTimeDatabase()?.child(Constants.ONGOING)?.addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (counterFirst == 0) {
                    ongoingTournaments.clear()
                }

                counterFirst +=1

                var tournament = p0.getValue(Tournament::class.java) as Tournament
                ongoingTournaments.add(tournament)
                rvTournamentPlayerAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var tournament = p0.getValue(Tournament::class.java) as Tournament

                for (currTournament in ongoingTournaments) {
                    if (tournament.id == currTournament.id) {
                        tournament = currTournament
                    }
                }

                ongoingTournaments.remove(tournament)
                rvTournamentPlayerAdapter?.notifyDataSetChanged()
            }

        })

    }
}

