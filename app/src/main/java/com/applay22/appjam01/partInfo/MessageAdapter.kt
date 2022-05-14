package com.applay22.appjam01.partInfo

import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applay22.appjam01.partInfo.databinding.ActivityListItemBinding

class MessageAdapter(private var data:Array<String>,private var dist:Array<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.MessageViewHolder {
        val binding=ActivityListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        holder.listitem.text=data[position]
        val item=dist[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
    inner class MessageViewHolder(private val binding: ActivityListItemBinding) : RecyclerView.ViewHolder(binding.root){
        var listitem=binding.textView6
        var test=binding.recentkm
        init{
            test.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    dist[adapterPosition]=p0.toString()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            })
        }
    }
}

}