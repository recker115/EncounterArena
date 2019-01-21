package com.apro.recky.battleSpree.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.Tournament
import com.apro.recky.battleSpree.models.User
import com.apro.recky.battleSpree.views.adapter.RvPlayerTournamentAdapter
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

                for (eachSnapshot in p0.child(Constants.USERS_JOINED).children) {
                    var user = eachSnapshot.getValue(User::class.java) as User
//                    usersJoined.add(user)
                    if (user.id == currUserID)
                        tournament.isCurrentUserJoined = true
                }

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

