package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item (name: String, description: String, userId : Int){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "description")
    var description : String

    @ColumnInfo(name = "userId")
    var userId : Int

    init {
//        this.id = id
        this.name = name
        this.description = description
        this.userId = userId
    }
}