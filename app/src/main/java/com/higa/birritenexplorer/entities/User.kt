package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User (name : String , password : String, email : String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "password")
    var password : String

    @ColumnInfo(name = "email")
    var email : String

    init {
        this.name = name
        this.password = password
        this.email = email

    }
}