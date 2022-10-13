package com.higa.birritenexplorer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item (name: String, userId : Int, description: String, imageUri : String){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "description")
    var description : String

    @ColumnInfo(name = "userId")
    var userId : Int

    @ColumnInfo(name = "imageUri")
    var imageUri : String

    init {
        this.name = name
        this.userId = userId
        this.description = description
        this.imageUri = imageUri
    }
}