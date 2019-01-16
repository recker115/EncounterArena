package com.example.shantunu.encounterarena.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.Utils
import com.google.firebase.database.DatabaseReference
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading_view.*

class Login : AppCompatActivity() , StickySwitch.OnSelectedChangeListener {

    var mDatebaseRef : DatabaseReference ?= null
    var isSignUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initMembers()
    }

    private fun initMembers() {
        stickySwitch.onSelectedChangeListener = this

        btnLogin.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

            if (isPlayerValidated()) {
                processLogin()
            }

        }

        mDatebaseRef = AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
    }

    private fun processLogin() {
        Utils.showProgressBar(pbMain, loadingView, this)
        pbMain.indeterminateDrawable = FoldingCirclesDrawable.Builder(this).build()
        if (isSignUp) {
            createNewUser()
        } else {
            loginExistingUser()
        }
    }

    private fun loginExistingUser() {
        AppClass.getAppInstance()?.getFirebaseAuth()?.signInWithEmailAndPassword(etEmail.text.toString(),
            etPassword.text.toString())?.addOnCompleteListener { task2 ->
            run {

                Utils.hideProgressBar(loadingView)
                if (task2.isSuccessful) {
                    startActivity(Intent(this@Login, PlayerActvity::class.java))
                    finish()
                }
                else {
                    // handle error
                    etPassword.error = "Password / Email is incorrect."
                }
            }
        }
    }

    private fun createNewUser() {
        AppClass.getAppInstance()?.getFirebaseAuth()?.createUserWithEmailAndPassword(etEmail.text.toString(),
            etPassword.text.toString())?.addOnCompleteListener { task2 ->
            run {

                Utils.hideProgressBar(loadingView)

                if (task2.isSuccessful) {
                    var uuId = AppClass.getAppInstance()?.getFirebaseAuth()?.currentUser?.uid
                    uuId?.let {

                        var userMap : LinkedHashMap<String, String> = LinkedHashMap()
                        userMap[Constants.EMAIL] = etEmail.text.toString()
                        userMap[Constants.PASSWORD] = etPassword.text.toString()
                        userMap[Constants.UUID] = it

                        mDatebaseRef?.child(it)?.setValue(userMap)?.addOnCompleteListener { task1 ->
                            run {
                                if (task1.isSuccessful) {
                                    startActivity(Intent(this@Login, PlayerActvity::class.java))
                                    finish()
                                }
                            }
                        }

                    }
                }
                else {
                    // handle error
                    etPassword.error = "Password is not in proper format."
                }
            }
        }
    }

    private fun isPlayerValidated(): Boolean {
        when{
            etEmail.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                return false
            }
            etPassword.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                return false
            }
            isSignUp -> {
                if (etPassword.text.toString() != etConfirmPassword.text.toString() ) {
                    etPassword.error = "Passwords do not match."
                    return false
                }
            }
        }
        return true
    }

    override fun onSelectedChange(direction: StickySwitch.Direction, text: String) {
        isSignUp = text == getString(R.string.new_player)

        if (isSignUp) {
            btnLogin.text = getString(R.string.only_signup)
            confirmPassword.visibility = View.VISIBLE
        }
        else{
            btnLogin.text = getString(R.string.only_signin)
            confirmPassword.visibility = View.GONE
        }
    }
}
