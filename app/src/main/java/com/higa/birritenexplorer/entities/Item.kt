package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item (qrId: String, userUID : String, album : String, imageUri : String, enabled : Boolean = true){
    @PrimaryKey()
    @ColumnInfo(name = "album")
    var album : String

    @ColumnInfo(name = "qrId")
    var qrId : String

    @ColumnInfo(name = "userUID")
    var userUID : String

    @ColumnInfo(name = "imageUri")
    var imageUri : String

    @ColumnInfo(name = "enabled")
    var enabled : Boolean

    init {
        this.album = album
        this.qrId = qrId
        this.userUID = userUID
        this.imageUri = imageUri
        this.enabled = enabled
    }
}