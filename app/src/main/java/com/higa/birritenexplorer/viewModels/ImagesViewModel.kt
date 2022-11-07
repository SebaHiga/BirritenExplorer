package com.higa.birritenexplorer.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.higa.birritenexplorer.entities.Item
import java.io.File

class ImagesViewModel : ViewModel(){
    var itemList : MutableList<Item> = mutableListOf()
    var toUploadList : MutableList<Item> = mutableListOf()
    private val firestoreDB = Firebase.firestore
    var localIsUpdated : Boolean = false
    var previousUID : String = "none"

    fun loadForUserUID(userUID : String){

        if (previousUID == userUID && localIsUpdated){
            listener()
            return
        }

        var docRef = firestoreDB.collection("images")
        docRef.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                add(Item(data["album"].toString(), data["userUID"].toString(), data["imageURI"].toString()))
            }
            localIsUpdated = true
            listener()
        }
    }

    private var listener : () -> Unit = {}

    fun setOnLoadListener (callback : () -> Unit){
        listener = callback
    }

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

    fun addLocal(item : Item){
        localIsUpdated = false
        itemList.add(item)
        toUploadList.add(item)
    }

    fun areItemsEqual(lhv : Item, rhv : Item) : Boolean{
        return lhv.imageUri == rhv.imageUri && rhv.album == lhv.album
    }

    fun uploadPending(){
        Log.d("ITEM VIEW CONTROLLER", "UPLOAD SIMULAING HEREE")
        for (item in toUploadList){
            var file = Uri.parse(item.imageUri)
            val imageReference = Firebase.storage.reference.child("${item.userUID}/${item.album}/${file.lastPathSegment}")

            var uploadTask = imageReference.putFile(file)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.d("UPLOAD UPLOAAAAD", "UPLOAD FUFCKING FAILEEEED :((((:LDJ!@#!!!!")
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Log.d("UPLOAD UPLOAAAAD", "FFUCK YEAAAAHFUCK YEAAAAHFUCK YEAAAAHFUCK YEAAAAHUCK YEAAAAH IMAGE REFERENCE IS ${imageReference.toString()}")
                val data = hashMapOf(
                    "album" to item.album,
                    "imageURI" to imageReference.toString(),
                    "userUID" to item.userUID,
                )
                firestoreDB.collection("images").add(data)
                    .addOnSuccessListener { Log.d("UPLOAD", "DDocumentSnapshot successfully written!DocumentSnapshot successfully written!DocumentSnapshot successfully written!ocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("UPLOAD", "EError writing documentError writing documentError writing documentError writing documentError writing documentrror writing document", e) }
            }
        }
    }

    fun filterByAlbum(album : String){
        var filtered : MutableList<Item> = mutableListOf()

        for (item in itemList) {
           if (album == item.album) {
               filtered.add((item))
           }
        }

        localIsUpdated = false

        itemList = filtered

        listener()
    }
}