package com.example.streamtv.streams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streamtv.R

class StreamsAdapter(private val context: Context, private val streams: List<Stream>) :
    RecyclerView.Adapter<StreamsAdapter.StreamsViewHolder>() {

    class StreamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = null
        var author: TextView? = null
        var watchingCount: TextView? = null
        var caption: ImageView? = null

        init {
            title = itemView.findViewById(R.id.streamTitle)
            author = itemView.findViewById(R.id.author)
            watchingCount = itemView.findViewById(R.id.watchingCount)
            caption = itemView.findViewById(R.id.caption)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.streams_item, parent, false)
        return StreamsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StreamsViewHolder, position: Int) {
        val item = streams[position]
        holder.title?.text = item.title
        holder.author?.text = item.author
        holder.watchingCount?.text = context.resources.getString(R.string.watching_now, item.watching)
        holder.caption?.let {
            Glide
                .with(context)
                .load(item.caption)
                .into(it)
        }
    }

    override fun getItemCount() = streams.size
}
