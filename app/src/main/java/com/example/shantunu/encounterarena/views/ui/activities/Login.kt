package com.example.shantunu.encounterarena.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.shantunu.encounterarena.AppClass
import com.example.shantunu.encounterarena.Constants
import com.example.shantunu.encounterarena.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() , StickySwitch.OnSelectedChangeListener {

    var isAdmin = false
    var mDatebaseRef : DatabaseReference ?= null

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
            } else {
                if (isValidatedPlayer()) {
                    processLogin()
                }
            }
        }

        mDatebaseRef = AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
    }

    private fun processLogin() {
        if (isAdmin) {

        } else {
            mDatebaseRef?.push()?.key?.let {
                var pushId : String = it
                var userMap : LinkedHashMap<String, String> = LinkedHashMap()
                userMap[Constants.EMAIL] = etEmail.text.toString()
                userMap[Constants.PASSWORD] = etPassword.text.toString()
                userMap[Constants.ID] = pushId

                mDatebaseRef?.setValue(userMap)?.addOnCompleteListener { task ->
                    run {
                        if (task.isSuccessful){
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString(),
                                etPassword.text.toString()).addOnCompleteListener { task2 ->
                                run {
                                    if (task2.isSuccessful)
                                        startActivity(Intent(this@Login, PlayerActvity::class.java))
                                    else {
                                        // handle error
                                    }
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
                false
            }
            etPassword.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                false
            }
        }
        return true
    }

    private fun isValidatedPlayer(): Boolean {
        when{
            etEmail.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                false
            }
            etPassword.text.toString().isEmpty() -> {
                etEmail.error = "Fill it"
                false
            }
            etConfirmPassword.text.toString().isEmpty() -> {
                etConfirmPassword.error = "Fill it"
                false
            }
            etPassword.text.toString() != etConfirmPassword.text.toString() -> {
                etPassword.error = "Passwords do not match"
                false
            }
        }
        return true
    }

    override fun onSelectedChange(direction: StickySwitch.Direction, text: String) {
        isAdmin = text == getString(R.string.admin)

        if (isAdmin) {
            confirmPassword.visibility = View.GONE
        }
        else
            confirmPassword.visibility = View.VISIBLE
    }
}
