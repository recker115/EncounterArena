package com.example.shantunu.encounterarena.firebaseModels

import com.example.shantunu.encounterarena.Constants
import com.google.firebase.database.PropertyName

class Tournament(@get:PropertyName(Constants.WINS_AMT) @set:PropertyName(Constants.WINS_AMT) var winPrice: String = "",
                 @get:PropertyName(Constants.IS_ROOM_CREATED) @set:PropertyName(Constants.IS_ROOM_CREATED) var isRoomCreated: String = "",
                 @get:PropertyName(Constants.ID) @set:PropertyName(Constants.ID) var id: String = "") {

    @PropertyName(Constants.TOURNY_NAME)
    var name : String ?= null
    @PropertyName(Constants.PER_KILL)
    var perKill : String ?= null
    @PropertyName(Constants.ENTRY_FEE)
    var entryFee : String ?= null
    @PropertyName(Constants.TYPE)
    var type : String ?= null
    @PropertyName(Constants.VERSION)
    var version : String ?= null
    @PropertyName(Constants.MAP)
    var map : String ?= null
    @PropertyName(Constants.TIMESTAMP)
    var timeStamp : String ?= null
    @PropertyName(Constants.ROOM_ID)
    var roomId : String ?= null
    @PropertyName(Constants.MAX_PLAYERS)
    var maxPlayers : String ?= null
    @PropertyName(Constants.PLAYERS_JOINED)
    var playersJoined : String ?= null

}