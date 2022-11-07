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

        var collection = firestoreDB.collection("images")
        collection.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
            itemList.clear()
            for (document in documents){
                var data = document.data
                itemList.add(Item(data["album"].toString(), data["userUID"].toString(), data["imageURI"].toString()))
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
        localIsUpdated = false
        itemList.add(item)
        toUploadList.add(item)
    }

    fun areItemsEqual(lhv : Item, rhv : Item) : Boolean{
        return lhv.imageUri == rhv.imageUri && rhv.album == lhv.album
    }

    fun uploadPending(){
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

    fun changeAlbumName(previousName : String, newName : String){
        for (item in itemList){
            Log.d("CHANGEALBUMNAME", "CHANGING TO $newName")
            if (previousName == item.album){
                item.album = newName
            }
        }
        for (item in toUploadList){
            if (previousName == item.album){
                item.album = newName
            }
        }

        Log.d("CHANGEALBUMNAME", "STARTIN CLOUD ALBUM NAME AAA")
        var collection = firestoreDB.collection("images")
        collection.whereEqualTo("album", previousName).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                Log.d("UPDATING", "UPDATING NEW DOCUMENT WITH OC ID ${document.id.toString()}")
                data["album"] = newName
                collection.document(document.id.toString()).set(data)
                    .addOnSuccessListener { Log.d("TAGANGNAANGNAGNAN", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("TAGANGNAANGNAGNAN", "Error writing document", e) }
            }
        }
        localIsUpdated = false
    }

    fun getByAlbum(album : String) : MutableList<Item>{
        var filtered : MutableList<Item> = mutableListOf()

        for (item in itemList) {
            Log.d("FFILTERFILTERFILTERFILTERFILTERFILTERILTER", "FILTERING ALBUM $album")
            if (album == item.album) {
               filtered.add((item))
           }
        }

        return filtered
    }
}