package com.apro.recky.battleSpree

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getDialog(context: Context, resourceId : Int): Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(resourceId)

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            return dialog
        }

        fun getDatePicker(context: Context, date : DatePickerDialog.OnDateSetListener, myCalendar : Calendar) : DatePickerDialog {
            val datePickerDialog = DatePickerDialog(
                context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )

            return datePickerDialog
        }

        fun getTimePicker(context: Context, time : TimePickerDialog.OnTimeSetListener, myCalendar: Calendar) : TimePickerDialog {
            val timePickerDialog = TimePickerDialog(
                context, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), true
            )

            return timePickerDialog
        }

        fun getAppDate(strEpoc : String) : String {
            var date = Date(strEpoc.toLong())
            var simpleDateFortmat = SimpleDateFormat("dd MMMM yyyy hh:mm aa")
            return simpleDateFortmat.format(date)
        }

        fun getCurrentUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        fun showProgressBar(progressBar : ProgressBar, loadingView : View, context: Context) {
            loadingView.visibility = View.VISIBLE
            progressBar.indeterminateDrawable = FoldingCirclesDrawable.Builder(context).build()
        }
        fun hideProgressBar(loadingView : View) {
            loadingView.visibility = View.GONE
        }

    }
}