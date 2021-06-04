package com.example.groupchat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessageViewModel : ViewModel() {

    class Factory : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessageViewModel() as T
        }
    }


}