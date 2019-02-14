package com.apro.recky.battleSpree.views.ui.activities.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.Constants
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.Utils
import com.apro.recky.battleSpree.models.Tournament
import com.apro.recky.battleSpree.views.adapter.RvTournamentsAdapter
import com.apro.recky.battleSpree.views.ui.activities.player.PlayerActvity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_admin_add_room.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

class AdminAddRoom : AppCompatActivity() {

    var vPrices : View ?= null
    var vType : View ?= null

    var etTournyName : TextInputEditText ?= null
//    var etRoomId : TextInputEditText ?= null
    var etDate : TextInputEditText ?= null

    var etWinsAmt: TextInputEditText ?= null
    var etPerKillAmt : TextInputEditText ?= null
    var etEntryFee : TextInputEditText ?= null

    var etType : TextInputEditText ?= null
    var etVersion : TextInputEditText ?= null
    var etMap : TextInputEditText ?= null

    var etWinsAmtLayout: TextInputLayout ?= null
    var etPerKillAmtLayout : TextInputLayout ?= null
    var etEntryFeeLayout : TextInputLayout ?= null

    var etTypeLayout: TextInputLayout ?= null
    var etVersionLayout : TextInputLayout ?= null
    var etMapLayout : TextInputLayout ?= null

    var btnCreateTourny : Button ?= null

    var rvTournyAdapter : RvTournamentsAdapter ?= null

    var dialogAdd : Dialog ?= null

    var tournaments : MutableList<Tournament> = mutableListOf()
    var counterFirst = 0

    private var myCalendar: Calendar  = Calendar.getInstance()

    private val date : DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {
            _, year, monthOfYear, dayOfMonth ->
        run {
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var date = sdf.format(myCalendar.time)
            etDate?.setText(date)
            etDate?.setSelection(date.length)

            Utils.getTimePicker(this@AdminAddRoom, time, Calendar.getInstance()).show()
        }
    }

    val time : TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener {
        _, hour, minute ->
        run {
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, minute)

            val myFormat = "hh:mm aa"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var date = etDate?.text.toString() + " "+ sdf.format(myCalendar.time)
            etDate?.setText(date)
            etDate?.setSelection(date.length)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_room)

        vSwitch.setOnClickListener{
            startActivity(Intent(this@AdminAddRoom, PlayerActvity::class.java))
        }
        initMembers()
    }

    private fun initMembers() {
        fabAdd.setOnClickListener {
            showDialog()
        }

        title = "Tournaments"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tournaments.add(Tournament())
        tournaments.add(Tournament())

        rvTournyAdapter = RvTournamentsAdapter(this, tournaments)
        rvTournaments.adapter = rvTournyAdapter
        rvTournaments.layoutManager = LinearLayoutManager(this@AdminAddRoom)

        btnResults.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.button_shrink))
            startActivity(Intent(this@AdminAddRoom, ResultsActivity::class.java))
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

                if (tournament.isOngoing) {
                    AppClass.APPINSTANCE?.getRealTimeDatabase()?.child(Constants.RESULTS)
                        ?.child(p0.key.toString())?.setValue(p0.value)
                    AppClass.APPINSTANCE?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)?.child(tournament.id)?.removeValue()
                }
                else {
                    for (currTournament in tournaments) {
                        if (tournament.id == currTournament.id) {
                            currTournament.isRoomCreated = tournament.isRoomCreated
                            currTournament.youtubeLink = tournament.youtubeLink
                            currTournament.roomId = tournament.roomId
                        }
                    }
                }

                rvTournyAdapter?.notifyDataSetChanged()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (counterFirst == 0) {
                    tournaments.clear()
                }

                counterFirst +=1

                var tournament = p0.getValue(Tournament::class.java) as Tournament
                tournaments.add(tournament)
                rvTournyAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var tournament = p0.getValue(Tournament::class.java) as Tournament

                for (currTournament in tournaments) {
                    if (tournament.id == currTournament.id) {
                        tournament = currTournament
                    }
                }

                tournaments.remove(tournament)
                rvTournyAdapter?.notifyDataSetChanged()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var datePicker: DatePickerDialog ?= null

    private var etMaxPlayers: TextInputEditText ?= null

    @SuppressLint("ClickableViewAccessibility")
    private fun showDialog() {
        dialogAdd = Utils.getDialog(this, R.layout.dialog_add)
        dialogAdd?.show()

        vPrices = dialogAdd?.findViewById(R.id.vPrices)
        vType = dialogAdd?.findViewById(R.id.vType)

        etTournyName = dialogAdd?.findViewById(R.id.etTournyName)
//        etRoomId = dialogAdd?.findViewById(R.id.etRoomId)
        etDate = dialogAdd?.findViewById(R.id.etDateTime)
        datePicker = Utils.getDatePicker(this@AdminAddRoom, date, Calendar.getInstance())
        etDate?.setOnTouchListener { v, event ->
            datePicker?.let {
                if (!it.isShowing) {
                    datePicker?.show()
                }
            }
            v?.onTouchEvent(event) ?: false
        }

        etWinsAmt = vPrices?.findViewById(R.id.etWinMoney) as TextInputEditText
        etPerKillAmt = vPrices?.findViewById(R.id.etPerkillMoney) as TextInputEditText
        etEntryFee = vPrices?.findViewById(R.id.etEntryFee) as TextInputEditText

        etWinsAmt?.inputType = InputType.TYPE_CLASS_NUMBER
        etPerKillAmt?.inputType = InputType.TYPE_CLASS_NUMBER
        etEntryFee?.inputType = InputType.TYPE_CLASS_NUMBER

        etWinsAmtLayout = vPrices?.findViewById(R.id.textInputLayout1) as TextInputLayout
        etPerKillAmtLayout = vPrices?.findViewById(R.id.textInputLayout2) as TextInputLayout
        etEntryFeeLayout = vPrices?.findViewById(R.id.textInputLayout3) as TextInputLayout

        etType = vType?.findViewById(R.id.etWinMoney) as TextInputEditText
        etVersion = vType?.findViewById(R.id.etPerkillMoney) as TextInputEditText
        etMap = vType?.findViewById(R.id.etEntryFee) as TextInputEditText

        etTypeLayout = vType?.findViewById(R.id.textInputLayout1) as TextInputLayout
        etVersionLayout = vType?.findViewById(R.id.textInputLayout2) as TextInputLayout
        etMapLayout = vType?.findViewById(R.id.textInputLayout3) as TextInputLayout

        etMaxPlayers = dialogAdd?.findViewById<View>(R.id.etMaxPlayers) as TextInputEditText

        btnCreateTourny = dialogAdd?.findViewById(R.id.btnCreateTourny) as Button

        btnCreateTourny?.setOnClickListener {
            validateAndProceed()
        }

        setHints()

    }

    private fun validateAndProceed() {
        when {
            etTournyName?.text.toString().isEmpty() -> etTournyName?.error = "Fill it"
            etWinsAmt?.text.toString().isEmpty() -> etWinsAmt?.error = "Fill it"
            etPerKillAmt?.text.toString().isEmpty() -> etPerKillAmt?.error = "Fill it"
            etEntryFee?.text.toString().isEmpty() -> etEntryFee?.error = "Fill it"
            etType?.text.toString().isEmpty() -> etType?.error = "Fill it"
            etVersion?.text.toString().isEmpty() -> etVersion?.error = "Fill it"
            etMap?.text.toString().isEmpty() -> etMap?.error = "Fill it"
//            etRoomId?.text.toString().isEmpty() -> etRoomId?.error = "Fill it"
            etDate?.text.toString().isEmpty() -> etDate?.error = "Fill it"
            etMaxPlayers?.text.toString().isEmpty() -> etMaxPlayers?.error = "Fill it"
            else -> {
                processAddTourny()
                dialogAdd?.dismiss()
            }
        }
    }

    private fun processAddTourny() {
        var databaseRefTournaments = AppClass.getAppInstance()?.getRealTimeDatabase()?.child(Constants.TOURNAMENTS)

        val tournyFields = LinkedHashMap<String, String>()
        tournyFields[Constants.TOURNY_NAME] = etTournyName?.text.toString()
        tournyFields[Constants.WINS_AMT] = etWinsAmt?.text.toString()
        tournyFields[Constants.PER_KILL] = etPerKillAmt?.text.toString()
        tournyFields[Constants.ENTRY_FEE] = etEntryFee?.text.toString()
        tournyFields[Constants.TYPE] = etType?.text.toString()
        tournyFields[Constants.VERSION] = etVersion?.text.toString()
        tournyFields[Constants.MAP] = etMap?.text.toString()
//        tournyFields[Constants.ROOM_ID] = etRoomId?.text.toString()
        tournyFields[Constants.TIMESTAMP] = myCalendar.time.time.toString()
        tournyFields[Constants.MAX_PLAYERS] = etMaxPlayers?.text.toString()
        tournyFields[Constants.PLAYERS_JOINED] = "0"
        tournyFields[Constants.IS_ROOM_CREATED] = "false"

        var pushId : String? = databaseRefTournaments?.push()?.key
        pushId?.let {
            tournyFields[Constants.ID] = it
            databaseRefTournaments?.child(pushId)?.setValue(tournyFields)
        }

    }

    private fun setHints() {
        etWinsAmtLayout?.hint = "Win Price (INR)"
        etPerKillAmtLayout?.hint = "Per Kill (INR)"
        etEntryFeeLayout?.hint = "Entry Fee (INR)"

        etTypeLayout?.hint = "Type"
        etVersionLayout?.hint = "Version"
        etMapLayout?.hint = "Map"
    }
}
