package com.higa.birritenexplorer.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.recyclerview.widget.RecyclerView
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item
import com.squareup.picasso.Picasso
import java.lang.reflect.Array.get
import java.nio.file.Paths.get

class ItemAdapter (private var itemList : MutableList<Item>, private val listener: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    class ItemHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View
        init {
            this.view = v
        }

        fun setName(name : String){
            var txtName : TextView = view.findViewById(R.id.textProfileName)
            txtName.text = name
        }

        fun setImageUri(imageUri : String){
            var imageView : ImageView = view.findViewById(R.id.itemImageView)
            if (imageUri != "null"){
                Picasso.get().load("https://i.stack.imgur.com/fh6LL.png").into(imageView);
//                imageView.setImageURI(Uri.parse("https://i.stack.imgur.com/fh6LL.png"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return (ItemHolder(view))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setName(itemList[position].name)
        holder.setImageUri(itemList[position].imageUri)
        holder.itemView.setOnClickListener { listener(itemList[position]) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}