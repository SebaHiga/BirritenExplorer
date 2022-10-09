package com.higa.birritenexplorer.controllers

import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.entities.User

class UserController (dao : UserDao?){

    var dao : UserDao?

    fun isValid(user : User) : Boolean {
        var foundUser = dao?.loadByName(user.name)

        return foundUser?.name == user.name && foundUser?.password == user.password
    }

    init {
        this.dao = dao
    }
}