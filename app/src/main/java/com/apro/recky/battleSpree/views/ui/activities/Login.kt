package com.apro.recky.battleSpree.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
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

        tvForgotPass.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            if (!EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
                Utils.displayLongToast("Please fill the email first..", this@Login)
            } else {
                performForgotPass()
            }
        }

        mDatebaseRef = AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.USERS)
    }

    private fun performForgotPass() {
        AppClass.getAppInstance()?.getFirebaseAuth()?.sendPasswordResetEmail(etEmail.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.displayLongToast("Password reset email sent ! Check Email", this@Login)
                } else {
                    Utils.displayLongToast("Email id not registered !", this@Login)
                }
            }
    }

    private fun processLogin() {
        Utils.showProgressBar(loadingView, this)
        pbMain.indeterminateDrawable = FoldingCirclesDrawable.Builder(this@Login).build()

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
                    AppClass.getAppInstance()?.generateUuid()
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
                        userMap[Constants.PHONE_NUMBER] = etPhoneNumber.text.toString()
                        userMap[Constants.WALLET_AMOUNT] = "0"
                        userMap[Constants.FULL_NAME] = etName.text.toString()

                        mDatebaseRef?.child(it)?.setValue(userMap)?.addOnCompleteListener { task1 ->
                            run {
                                if (task1.isSuccessful) {
                                    AppClass.getAppInstance()?.generateUuid()
                                    startActivity(Intent(this@Login, PlayerActvity::class.java))
                                    finish()
                                }
                            }
                        }

                    }
                }
                else {
                    // handle error
                    etPassword.error = task2.exception?.message
                }
            }
        }
    }

    private fun isPlayerValidated(): Boolean {
        when{
            !EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches() -> {
                etEmail.error = "Need proper email"
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
                } else if (!PHONE.matcher(etPhoneNumber.text.toString()).matches()) {
                    etPhoneNumber.error = "Need proper phone"
                    return false
                } else if(etName.text.toString().isEmpty()){
                    etPhoneNumber.error = "Name is mandatory!"
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
            phone.visibility = View.VISIBLE
            name.visibility = View.VISIBLE
            tvForgotPass.text = ""
            tvForgotPass.isEnabled = false
        }
        else{
            btnLogin.text = getString(R.string.only_signin)
            confirmPassword.visibility = View.GONE
            phone.visibility = View.GONE
            name.visibility = View.GONE
            tvForgotPass.text = "Forgot Password ?"
            tvForgotPass.isEnabled = true
        }
    }
}
