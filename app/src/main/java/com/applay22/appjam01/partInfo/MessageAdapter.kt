package com.applay22.appjam01.partInfo

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applay22.appjam01.partInfo.databinding.ActivityListItemBinding

class MessageAdapter(private var data:Array<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.MessageViewHolder {
        val binding=ActivityListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        holder.listitem.text=data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
    inner class MessageViewHolder(private val binding: ActivityListItemBinding) : RecyclerView.ViewHolder(binding.root){
        var listitem=binding.textView6
    }
}