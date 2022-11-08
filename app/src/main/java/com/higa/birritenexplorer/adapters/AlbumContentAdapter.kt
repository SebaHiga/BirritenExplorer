package com.higa.birritenexplorer.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item
import com.squareup.picasso.Picasso

class AlbumContentAdapter (private var itemList : MutableList<Item>) : RecyclerView.Adapter<AlbumContentAdapter.ItemHolder>() {

    class ItemHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View
        init {
            this.view = v
        }

        fun setImageUri(imageUri : String){
            var imageView : ImageView = view.findViewById(R.id.albumImageView)
            if (imageUri != "null"){
                if (imageUri.startsWith("gs")){
                    Firebase.storage.getReferenceFromUrl(imageUri).downloadUrl.addOnSuccessListener { url ->
                        Log.d("ADAPTER AAA", "IMAGE URI IS $url")
                        Picasso.get().load(url).into(imageView);
                    }
                }
                else{
                    Log.d("ADAPTER AAA", "IMAGE URI IS $imageUri")
                    Picasso.get().load(imageUri).into(imageView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return (ItemHolder(view))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setImageUri(itemList[position].imageUri)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}