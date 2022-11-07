package com.higa.birritenexplorer.viewModels

import androidx.lifecycle.ViewModel
import com.higa.birritenexplorer.entities.Item

class ImagesViewModel : ViewModel(){
    var itemList : MutableList<Item> = mutableListOf()

    fun add(item : Item){
        var found = false

        for (i in itemList){
            if (areItemsEqual(i, item)) {
                found = true
                break
            }
        }

        if (!found){
            itemList.add(item)
        }
    }

    fun areItemsEqual(lhv : Item, rhv : Item) : Boolean{
        return lhv.imageUri == rhv.imageUri && rhv.album == lhv.album
    }

    fun getByAlbum(album : String) : MutableList<Item>{
        var ret : MutableList<Item> = mutableListOf()

        for (item in itemList) {
           if (album == item.album) {
               ret.add((item))
           }
        }

        return ret
    }
}