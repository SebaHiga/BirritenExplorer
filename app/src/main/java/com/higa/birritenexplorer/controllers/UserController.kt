package com.higa.birritenexplorer.controllers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.entities.User
import org.mindrot.jbcrypt.BCrypt

class UserController (dao : UserDao?){

    var dao : UserDao?

    fun isValid(user : User) : Boolean {
        var foundUser = dao?.loadByName(user.name)

        val hashedPassword = foundUser?.password.toString()
        val passOk = BCrypt.checkpw(user.password, hashedPassword)

        return foundUser?.name == user.name && passOk
    }

    fun insert(user : User) {
        val pass = user.password
        val hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());

        dao?.insert(User(user.name, hashedPassword, user.email))
    }

    init {
        this.dao = dao
    }
}