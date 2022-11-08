package com.higa.birritenexplorer.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item
import com.squareup.picasso.Picasso

class ItemAdapter (private var itemList : MutableList<Item>, private var listener : (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    class ItemHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View
        init {
            this.view = v
        }

        fun setName(name : String){
//            var txtName : TextView = view.findViewById(R.id.textProfileName)
//            txtName.text = name
        }

        fun setImageUri(imageUri : String){
            var imageView : ImageView = view.findViewById(R.id.albumImageView)
            if (imageUri != "null"){
                if (imageUri.startsWith("gs")){
                    Firebase.storage.getReferenceFromUrl(imageUri).downloadUrl.addOnSuccessListener { url ->
                        Picasso.get().load(url).placeholder(R.drawable.progress_animation).into(imageView);
                    }
                }
                else{
                    Picasso.get().load(imageUri).placeholder(R.drawable.progress_animation).into(imageView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return (ItemHolder(view))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setName(itemList[position].album)
        holder.setImageUri(itemList[position].imageUri)
        holder.itemView.setOnClickListener { listener(itemList[position]) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}