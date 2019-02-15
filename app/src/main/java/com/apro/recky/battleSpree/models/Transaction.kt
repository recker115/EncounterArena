package com.apro.recky.battleSpree.models

import com.apro.recky.battleSpree.Constants
import com.google.firebase.database.PropertyName

class Transaction(@get:PropertyName(Constants.IS_WITHDRAW) @set:PropertyName(Constants.IS_WITHDRAW) var isWithdraw: String = "",
                  @get:PropertyName(Constants.AMOUNT) @set:PropertyName(Constants.AMOUNT) var amount: String = "",
                  @get:PropertyName(Constants.TIMESTAMP) @set:PropertyName(Constants.TIMESTAMP) var timeStamp: String = "",
                  @get:PropertyName(Constants.DEPOSITED_BY) @set:PropertyName(Constants.DEPOSITED_BY) var depositedBy: String = "")