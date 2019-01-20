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
import com.example.shantunu.encounterarena.models.User
import com.example.shantunu.encounterarena.views.adapter.RvPlayerTournamentAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_player_tournaments.*

class PlayerTournaments : Fragment() {

    var tournaments = mutableListOf<Tournament>()
    var rvTournamentPlayerAdapter : RvPlayerTournamentAdapter ?= null
    var counterFirst = 0
    var currUserID : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_tournaments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMembers()
    }

    private fun initMembers() {

        tournaments.add(Tournament())
        tournaments.add(Tournament())
        tournaments.add(Tournament())

        Utils.getCurrentUser()?.uid ?.let {
            currUserID = it
        }

        activity?.let {
            rvTournamentPlayerAdapter = RvPlayerTournamentAdapter(it, tournaments, currUserID)
            rvTournaments.adapter = rvTournamentPlayerAdapter
            rvTournaments.layoutManager = LinearLayoutManager(it)
        }

        listenTournamentDatabase()
    }

    private fun listenTournamentDatabase() {
        AppClass.APPINSTANCE?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var tournament = p0.getValue(Tournament::class.java) as Tournament

//                var usersJoined = mutableListOf<User>()
                    for (eachSnapshot in p0.child(Constants.USERS_JOINED).children) {
                        var user = eachSnapshot.getValue(User::class.java) as User
//                    usersJoined.add(user)
                        if (user.id == currUserID)
                            tournament.isCurrentUserJoined = true
                    }

                    for (currTournament in tournaments) {
                        if (tournament.id == currTournament.id) {
                            currTournament.isRoomCreated = tournament.isRoomCreated
                            currTournament.playersJoined = tournament.playersJoined
//                        currTournament.listOfUsersJoined = usersJoined
                            currTournament.isCurrentUserJoined = tournament.isCurrentUserJoined
                        }
                    }


                rvTournamentPlayerAdapter?.notifyDataSetChanged()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (counterFirst == 0) {
                    tournaments.clear()
                }

                counterFirst +=1

                var tournament = p0.getValue(Tournament::class.java) as Tournament
//                var usersJoined = mutableListOf<User>()

                for (eachSnapshot in p0.child(Constants.USERS_JOINED).children) {
                    var user = eachSnapshot.getValue(User::class.java) as User
//                    usersJoined.add(user)
                    if (user.id.equals(currUserID))
                        tournament.isCurrentUserJoined = true
                }

//                tournament.listOfUsersJoined = usersJoined
                tournaments.add(tournament)
                rvTournamentPlayerAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var tournament = p0.getValue(Tournament::class.java) as Tournament

                for (currTournament in tournaments) {
                    if (tournament.id == currTournament.id) {
                        tournament = currTournament
                    }
                }

                tournaments.remove(tournament)
                rvTournamentPlayerAdapter?.notifyDataSetChanged()
            }

        })
    }

}
