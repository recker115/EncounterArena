package com.example.shantunu.encounterarena.views.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() , StickySwitch.OnSelectedChangeListener {

    var isAdmin = false
    var mDatebaseRef : DatabaseReference ?= null
    var isSignUp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initMembers()
    }

    private fun initMembers() {
        stickySwitch.onSelectedChangeListener = this

        btnLogin.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

            if (isAdmin) {
                if (isValidated()) {
                    processLogin()
                }
            }
            else {
                if (isPlayerValidated()) {
                    processLogin()
                }
            }
        }

        tvIsSignUp.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))

            if (isSignUp) {
                btnLogin.text = getString(R.string.only_signin)
                tvIsSignUp.text = getString(R.string.signup)
                isSignUp = !isSignUp
                confirmPassword.visibility = View.GONE
            } else {
                btnLogin.text = getString(R.string.only_signup)
                tvIsSignUp.text = getString(R.string.signin)
                isSignUp = !isSignUp
                confirmPassword.visibility = View.VISIBLE
            }

        }

        mDatebaseRef = AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
    }

    private fun processLogin() {
        if (isAdmin) {
            if (etEmail.text.toString() == "Approx@htmail.com" && etPassword.text.toString() == "Approx"){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.text.toString(),
                    etPassword.text.toString()).addOnCompleteListener { task2 ->
                    run {
                        if (task2.isSuccessful) {
                            getSharedPreferences(Constants.APP_SHARED_PREF, Context.MODE_PRIVATE).edit().putBoolean(Constants.IS_ADMIN, true).apply()
                            startActivity(Intent(this@Login, AdminAddRoom::class.java))
                            finish()
                        }
                        else {
                            // handle error

                        }
                    }
                }

            }

        } else {
            if (isSignUp) {
               createNewUser()
            } else {
                loginExistingUser()
            }
        }
    }

    private fun loginExistingUser() {
        AppClass.getAppInstance()?.getFirebaseAuth()?.signInWithEmailAndPassword(etEmail.text.toString(),
            etPassword.text.toString())?.addOnCompleteListener { task2 ->
            run {
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
        mDatebaseRef?.push()?.key?.let {
            var pushId : String = it
            var userMap : LinkedHashMap<String, String> = LinkedHashMap()
            userMap[Constants.EMAIL] = etEmail.text.toString()
            userMap[Constants.PASSWORD] = etPassword.text.toString()
            userMap[Constants.ID] = pushId

            mDatebaseRef?.child(pushId)?.setValue(userMap)?.addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful){
                        AppClass.getAppInstance()?.getFirebaseAuth()?.createUserWithEmailAndPassword(etEmail.text.toString(),
                            etPassword.text.toString())?.addOnCompleteListener { task2 ->
                            run {
                                if (task2.isSuccessful) {
                                    startActivity(Intent(this@Login, PlayerActvity::class.java))
                                    finish()
                                }
                                else {
                                    // handle error
                                    etPassword.error = "Password is not in proper format."
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isValidated(): Boolean {
        when{
            etEmail.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                return false
            }
            etPassword.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                return false
            }
        }
        return true
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
        isAdmin = text == getString(R.string.admin)

        if (isAdmin) {
            tvIsSignUp.text = ""
            tvIsSignUp.isEnabled = false
            btnLogin.text = getString(R.string.only_signin)
            confirmPassword.visibility = View.GONE
        }
        else{
            tvIsSignUp.isEnabled = true
            if (isSignUp){
                btnLogin.text = getString(R.string.only_signup)
                tvIsSignUp.text = getString(R.string.signin)
                confirmPassword.visibility = View.VISIBLE
            }
            else{
                btnLogin.text = getString(R.string.only_signin)
                tvIsSignUp.text = getString(R.string.signup)
                confirmPassword.visibility = View.GONE
            }
        }
    }
}
