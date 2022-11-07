package com.higa.birritenexplorer.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.higa.birritenexplorer.entities.Item

class ImagesViewModel : ViewModel(){
    var itemList : MutableLiveData<MutableList<Item>> = MutableLiveData(mutableListOf())
    var toUploadList : MutableList<Item> = mutableListOf()
    private val firestoreDB = Firebase.firestore
    var localIsUpdated : Boolean = false
    var previousUID : String = "none"

    fun loadForUserUID(userUID : String){

        if (previousUID == userUID && localIsUpdated){
            listener()
            return
        }
        var tmp : MutableList<Item> = mutableListOf()

        var docRef = firestoreDB.collection("images")
        docRef.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                tmp.add(Item(data["album"].toString(), data["userUID"].toString(), data["imageURI"].toString()))
            }
            itemList.value = tmp
            localIsUpdated = true
            listener()
        }
    }
    private var listener: () -> Unit = {}

    fun addOnLoadListener(callback: () -> Unit){
        listener = callback
    }

    fun add(item : Item){
        var found = false

        for (i in itemList.value!!){
            if (areItemsEqual(i, item)) {
                found = true
                break
            }
        }

        if (!found){
            itemList.value!!.add(item)
        }
    }

    fun addLocal(item : Item){
        localIsUpdated = false
        itemList.value!!.add(item)
        toUploadList.add(item)
    }

    fun areItemsEqual(lhv : Item, rhv : Item) : Boolean{
        return lhv.imageUri == rhv.imageUri && rhv.album == lhv.album
    }

    fun uploadPending(){
        Log.d("ITEM VIEW CONTROLLER", "UPLOAD SIMULAING HEREE")
    }

    fun filterByAlbum(album : String){
        var filtered : MutableList<Item> = mutableListOf()

        for (item in itemList.value!!) {
           if (album == item.album) {
               filtered.add((item))
           }
        }

        localIsUpdated = false

        itemList.value = filtered
    }
}