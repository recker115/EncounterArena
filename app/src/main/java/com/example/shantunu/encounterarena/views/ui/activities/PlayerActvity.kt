package com.example.shantunu.encounterarena.views.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.views.ui.fragments.MyProfile
import com.example.shantunu.encounterarena.views.ui.fragments.Notifications
import com.example.shantunu.encounterarena.views.ui.fragments.PlayerTournaments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_player_actvity.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class PlayerActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_actvity)

        initMembers()
    }

    private fun initMembers() {
        title = "Players"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigateToFragment(PlayerTournaments())
        setBottomNavigations()
        setFcmToken()
    }

    private fun setFcmToken() {

        var currentUserId = AppClass.getAppInstance()?.currentUuId
        if (currentUserId != null) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                var token = it.token
                AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
                    ?.child(currentUserId)?.child(Constants.FCM_TOKEN)?.setValue(token)
            }
        }
    }

    private fun setBottomNavigations() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.tournaments -> navigateToFragment(PlayerTournaments())
                R.id.notifications -> navigateToFragment(Notifications())
                R.id.myprofile -> navigateToFragment(MyProfile())
            }
            true
        }
    }

    private fun navigateToFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameContainer, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
