package com.apro.recky.battleSpree.models

import com.apro.recky.battleSpree.Constants
import com.google.firebase.database.PropertyName
class User(@get:PropertyName(Constants.EMAIL) @set:PropertyName(Constants.EMAIL) var email: String = "",
           @get:PropertyName(Constants.PASSWORD) @set:PropertyName(Constants.PASSWORD) var password: String = "",
           @get:PropertyName(Constants.ID) @set:PropertyName(Constants.ID) var id: String = "",
           @get:PropertyName(Constants.PUBG_ID) @set:PropertyName(Constants.PUBG_ID) var pubgId: String = "",
           @get:PropertyName(Constants.KILLS) @set:PropertyName(Constants.KILLS) var kills: String = "",
           @get:PropertyName(Constants.MONEY_WON) @set:PropertyName(Constants.MONEY_WON) var amtWon: String = "0")