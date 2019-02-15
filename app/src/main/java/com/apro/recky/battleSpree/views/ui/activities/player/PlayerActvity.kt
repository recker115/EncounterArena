package com.apro.recky.battleSpree.views.ui.activities.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.views.ui.fragments.Notifications
import com.apro.recky.battleSpree.views.ui.fragments.ResultsFragment
import com.apro.recky.battleSpree.views.ui.fragments.PlayerTournaments
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_player_actvity.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*

class PlayerActvity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var currentUserId : String ?= null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawer(GravityCompat.START)
        when(item.itemId) {
            R.id.nav_wallet -> {
                startActivity(Intent(this@PlayerActvity, UserWallet::class.java))
            }
            R.id.nav_logout -> {
                AppClass.getAppInstance()?.getFirebaseAuth()?.signOut()
                getSharedPreferences(Constants.APP_SHARED_PREF, Context.MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this, Splash::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                finish()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_actvity)

        initMembers()
    }

    private fun initMembers() {
        title = "Players"
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.mipmap.menu)
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentUserId = AppClass.getAppInstance()?.currentUuId

        navigateToFragment(PlayerTournaments())
        setBottomNavigations()
        setSideNavigation()
        setFcmToken()
    }

    private fun setSideNavigation() {
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        //set title
        AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)?.child(currentUserId.toString())
            ?.child(Constants.EMAIL)?.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var email : String = p0.value.toString()
                    var headerView : View = navView.getHeaderView(0)
                    val tvUserEmai : TextView = headerView.findViewById(R.id.tvUserEmail)
                    tvUserEmai.text = email
                }

            })
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
//                R.id.myprofile -> navigateToFragment(MyProfile())
                R.id.results -> navigateToFragment(ResultsFragment())
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
                drawer.openDrawer(GravityCompat.START)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.frameContainer)
        if (currentFragment is PlayerTournaments){
            super.onBackPressed()
        } else {
            navigateToFragment(PlayerTournaments())
            bottomNavigationView.selectedItemId = R.id.tournaments
        }
    }
}
