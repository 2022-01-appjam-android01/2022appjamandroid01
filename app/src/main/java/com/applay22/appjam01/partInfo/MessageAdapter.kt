package com.applay22.appjam01.partInfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applay22.appjam01.partInfo.databinding.ActivityListItemBinding

class MessageAdapter(private data:Array<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.MessageViewHolder {

    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

    }
    inner class MessageViewHolder(private val binding: ActivityListItemBinding) : RecyclerView.ViewHolder(binding.root){

    }
}