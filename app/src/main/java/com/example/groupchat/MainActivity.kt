package com.example.groupchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.groupchat.databinding.ActivityMainBinding
import com.example.groupchat.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseDatabase: FirebaseDatabase by lazy {
        Firebase.database
    }

    private val messageAdapter : MessageAdapter by lazy {
        MessageAdapter(layoutInflater)
    }

    private val messageList : ArrayList<Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMessagesFromDB()

        binding.messegeList.adapter = messageAdapter

        binding.sendButton.setOnClickListener {
            val name: String = binding.nameBox.text.toString().trim()
            val message: String = binding.messageBox.text.toString().trim()

            val formattedMessage: Message =
                if (name.isBlank())
                    Message(message = message)
                else
                    Message(name, message)

            uploadMessageOnDB(formattedMessage)
        }

    }

    private fun getMessagesFromDB() {
        val messageRef = firebaseDatabase.getReference("messages")

        messageRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesMap : HashMap<String, HashMap<String, String>> = snapshot.value as HashMap<String, HashMap<String, String>>
                messagesMap.map { messageMap ->
                    val message : Message =
                        Message(
                            name = messageMap.value["name"]!!,
                            message = messageMap.value["message"]!!
                        )

                    if (!messageList.contains(message))
                        messageList.add(message)
                }
                messageAdapter.submitList(messageList.toList())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun uploadMessageOnDB(message: Message) {
        val messageRef = firebaseDatabase.getReference("messages").push()
        messageRef.setValue(message)

        binding.messageBox.setText("")
    }
}