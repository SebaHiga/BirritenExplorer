package com.higa.birritenexplorer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item

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

        fun setDescription(name : String){
            var txtDescription : TextView = view.findViewById(R.id.textDescription)
            txtDescription.text = name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return (ItemHolder(view))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setName(itemList[position].name)
        holder.setDescription(itemList[position].description)
        holder.itemView.setOnClickListener { listener(itemList[position]) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}