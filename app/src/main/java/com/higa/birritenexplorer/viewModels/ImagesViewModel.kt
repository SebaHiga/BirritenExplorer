package com.higa.birritenexplorer.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.higa.birritenexplorer.entities.Item
import kotlinx.coroutines.tasks.await
import java.io.File

class ImagesViewModel : ViewModel(){
    var itemList : MutableList<Item> = mutableListOf()
    var toUploadList : MutableList<Item> = mutableListOf()
    private val firestoreDB = Firebase.firestore
    var localIsUpdated : Boolean = false

    fun loadForUserUID(userUID : String){
        if (localIsUpdated){
            listener()
            return
        }

        var collection = firestoreDB.collection("images")
        collection.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
            itemList.clear()
            for (document in documents){
                var data = document.data
                var imageURI = data["imageURI"]
                imageURI = Uri.parse(imageURI as String?).toString()
                itemList.add(Item(data["qrId"].toString(), data["userUID"].toString(), data["album"].toString(), imageURI))
            }
            localIsUpdated = true
            listener()
        }
    }

    private var listener : () -> Unit = {}

    fun setOnLoadListener (callback : () -> Unit){
        listener = callback
    }

    fun addLocal(item : Item){
//        localIsUpdated = false
        itemList.add(item)
        toUploadList.add(item)
    }

    fun areItemsEqual(lhv : Item, rhv : Item) : Boolean{
        return lhv.imageUri == rhv.imageUri && rhv.album == lhv.album
    }

    fun uploadPending(){
        for (item in toUploadList){
            var file = Uri.parse(item.imageUri)
            val imageReference = Firebase.storage.reference.child("${item.userUID}/${item.qrId}/${file.lastPathSegment}")

            var uploadTask = imageReference.putFile(file)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                val data = hashMapOf(
                    "album" to item.album,
                    "qrId" to item.qrId,
                    "imageURI" to imageReference.toString(),
                    "userUID" to item.userUID,
                )
                firestoreDB.collection("images").add(data)
                    .addOnSuccessListener { Log.d("UPLOAD", "Document added") }
                    .addOnFailureListener { e -> Log.w("UPLOAD", "Error adding new document", e) }
            }
        }
    }

    fun changeAlbumName(qrId : String, newName : String){
        var userUID : String = ""

        for (item in itemList){
            userUID = item.userUID
            if (qrId == item.qrId){
                item.album = newName
            }
        }
        for (item in toUploadList){
            if (qrId == item.qrId){
                item.album = newName
            }
        }

        // Update album data on each image
        var collectionImages = firestoreDB.collection("images")
        collectionImages.whereEqualTo("qrId", qrId).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                data["album"] = newName
                collectionImages.document(document.id.toString()).set(data)
            }
        }

        // Update QR Album map
        var collectionNoters = firestoreDB.collection("quicknoter")
        collectionNoters.whereEqualTo("userUID", userUID).whereEqualTo("qrId", qrId).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                data["album"] = newName
                collectionNoters.document(document.id.toString()).set(data)
            }
        }
    }

    fun getByQrId(qrId: String) : MutableList<Item>{
        var filtered : MutableList<Item> = mutableListOf()

        for (item in itemList) {
            if (qrId == item.qrId) {
               filtered.add((item))
           }
        }

        return filtered
    }

    fun getAlbumByIdentificatorsTask(userUID: String, qrId : String) : Task<QuerySnapshot> {
        var collection = firestoreDB.collection("quicknoter")
        return collection.whereEqualTo("userUID", userUID).whereEqualTo("qrId", qrId).get()
    }

    fun addNewQRId(userUID: String, qrId: String, album: String) {
        val data = hashMapOf(
            "album" to album,
            "qrId" to qrId,
            "userUID" to userUID,
        )
        firestoreDB.collection("quicknoter").add(data)
            .addOnSuccessListener { Log.d("UPLOAD", "Document added") }
            .addOnFailureListener { e -> Log.w("UPLOAD", "Error adding new document", e) }
    }

}