package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Album (qrId : String, album : String, content : MutableList<Item>){
    var album : String
    var qrId : String
    var content : MutableList<Item>

    init {
        this.qrId = qrId
        this.album = album
        this.content = content
    }
}