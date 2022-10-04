package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item (id: Int, name: String, description: String){
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : Int

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "description")
    var description : String

    init {
        this.id = id
        this.name = name
        this.description = description

    }
}