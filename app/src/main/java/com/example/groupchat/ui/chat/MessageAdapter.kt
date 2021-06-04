package com.example.groupchat.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupchat.databinding.MessageListItemBinding
import com.example.groupchat.model.MessageReceived

class MessageAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemLongClick : (MessageReceived) -> Unit
) : ListAdapter<MessageReceived, MessageAdapter.MessageViewHolder>(
    DiffUtilCallback
) {

    object DiffUtilCallback : DiffUtil.ItemCallback<MessageReceived>() {
        override fun areItemsTheSame(oldItem: MessageReceived, newItem: MessageReceived): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MessageReceived, newItem: MessageReceived): Boolean {
            return oldItem == newItem
        }
    }

    class MessageViewHolder(val binding : MessageListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            MessageListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.binding.name.text = message.name
        holder.binding.message.text = message.message

        holder.binding.root.setOnLongClickListener {
            onItemLongClick(message)
            true
        }

        holder.binding.executePendingBindings()
    }
}