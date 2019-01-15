package com.example.shantunu.encounterarena.views.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.Utils
import com.google.firebase.auth.FirebaseAuth
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {

    var mAuth : FirebaseAuth ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initMembers()
    }

    private fun initMembers() {
        progressBar.indeterminateDrawable = FoldingCirclesDrawable.Builder(this).build()

        splashProcess()
    }

    private fun splashProcess() {
        mAuth = AppClass.getAppInstance()?.getFirebaseAuth()
        var currentUser = Utils.getCurrentUser()

        if (currentUser != null) {
            openHome()
        } else {
            openLogin()
        }
    }

    private fun openLogin() {
        Handler().postDelayed({
            startActivity(Intent(this@Splash, Login::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
        }, 2000)
    }

    private fun openHome() {
        var isAdmin = getSharedPreferences(Constants.APP_SHARED_PREF, Context.MODE_PRIVATE).getBoolean(Constants.IS_ADMIN, false)

        if (isAdmin) {
           Handler().postDelayed({ startActivity(Intent(this@Splash, AdminAddRoom::class.java))
               finish()
           }, 2000)
        } else {
            Handler().postDelayed({ startActivity(Intent(this@Splash, PlayerActvity::class.java))
                finish()
            }, 2000)
        }
    }
}
