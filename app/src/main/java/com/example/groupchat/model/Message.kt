package com.example.groupchat.model

data class MessageReceived(
    val id : String,
    val name : String,
    val message : String
)

data class MessageSend(
    val name : String = "Anonymous",
    val message : String
)