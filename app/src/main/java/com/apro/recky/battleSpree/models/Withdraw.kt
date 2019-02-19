package com.apro.recky.battleSpree.models

import com.apro.recky.battleSpree.Constants
import com.google.firebase.database.PropertyName

class Withdraw(@get:PropertyName(Constants.REQUESTED_BY) @set:PropertyName(Constants.REQUESTED_BY) var email: String = "",
               @get:PropertyName(Constants.PHONE_NUMBER) @set:PropertyName(Constants.PHONE_NUMBER) var phnNumber: String = "",
               @get:PropertyName(Constants.AMOUNT) @set:PropertyName(Constants.AMOUNT) var amount: String = "",
               @get:PropertyName(Constants.IS_PAID) @set:PropertyName(Constants.IS_PAID) var isPaid: String = "",
               @get:PropertyName(Constants.ID) @set:PropertyName(Constants.ID) var id: String = "")