package com.higa.birritenexplorer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Album
import com.higa.birritenexplorer.entities.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.album.view.*
import kotlinx.android.synthetic.main.fragment_creation.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class SummaryAdapter (private var albumList : MutableList<Album>, private val listener: (Item) -> Unit) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album, parent, false)
        return (ViewHolder(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = albumList[position].album
//        holder.itemView.setOnClickListener { listener(albumList[position]) }
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = ItemAdapter(albumList[position].content, listener)
//            setOnClickListener { listener(albumList[position]) }
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_child
        var textView:TextView = itemView.textView3
    }

}