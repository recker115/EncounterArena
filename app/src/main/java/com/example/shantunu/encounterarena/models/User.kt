package com.example.shantunu.encounterarena.models

import com.example.shantunu.encounterarena.Constants
import com.google.firebase.database.PropertyName
class User(@get:PropertyName(Constants.EMAIL) @set:PropertyName(Constants.EMAIL) var email: String = "",
           @get:PropertyName(Constants.PASSWORD) @set:PropertyName(Constants.PASSWORD) var password: String = "",
           @get:PropertyName(Constants.ID) @set:PropertyName(Constants.ID) var id: String = "" )