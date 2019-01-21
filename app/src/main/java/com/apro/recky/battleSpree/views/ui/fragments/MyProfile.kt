package com.apro.recky.battleSpree.views.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.views.ui.activities.Splash
import kotlinx.android.synthetic.main.add_wallets.*
import kotlinx.android.synthetic.main.fragment_my_profile.*

class MyProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
    }

    private fun initMembers() {

        activity?.let {
            it.title = "Notifications"
        }

        folding_cell.setOnClickListener {
            folding_cell.toggle(false)
        }

        vLogout.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            AppClass.getAppInstance()?.getFirebaseAuth()?.signOut()

            it.context.getSharedPreferences(Constants.APP_SHARED_PREF,Context.MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(it.context, Splash::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            activity?.finish()
        }
    }

}
