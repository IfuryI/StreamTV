package com.example.streamtv.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamtv.R

class StreamsAdapter(private val streams: List<Stream>) :
    RecyclerView.Adapter<StreamsAdapter.StreamsViewHolder>() {

    class StreamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = null

        init {
            title = itemView.findViewById(R.id.streamTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.streams_item, parent, false)
        return StreamsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StreamsViewHolder, position: Int) {
        holder.title?.text = streams[position].title
    }

    override fun getItemCount() = streams.size
}