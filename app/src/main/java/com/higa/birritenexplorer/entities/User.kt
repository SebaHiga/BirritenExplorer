package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User (  id : Int, name : String , password : String, email : String) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : Int

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "password")
    var password : String

    @ColumnInfo(name = "email")
    var email : String

    init {
        this.id = id
        this.name = name
        this.password = password
        this.email = email

    }
}