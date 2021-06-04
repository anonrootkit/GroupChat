package com.example.groupchat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupchat.model.MessageReceived
import com.example.groupchat.model.MessageSend
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageViewModel : ViewModel() {

    private val _messages = MutableLiveData<ArrayList<MessageReceived>>()
    val messages: LiveData<ArrayList<MessageReceived>>
        get() = _messages

    private val _isMessageSent = MutableLiveData<Boolean>()
    val isMessageSent: LiveData<Boolean>
        get() = _isMessageSent

    private val firebaseDatabase: FirebaseDatabase by lazy {
        Firebase.database
    }

    fun messageSent() {
        _isMessageSent.value = null
    }

    fun getMessagesFromDB() {
        val messageRef = firebaseDatabase.getReference("messages")

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val currentList: ArrayList<MessageReceived> = _messages.value ?: ArrayList()

                val messagesMap: HashMap<String, String> =
                    snapshot.value as? HashMap<String, String> ?: return

                val message: MessageReceived =
                    MessageReceived(
                        id = snapshot.key!!,
                        name = messagesMap["name"]!!,
                        message = messagesMap["message"]!!
                    )

                currentList.add(message)

                _messages.value = currentList
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val currentList: ArrayList<MessageReceived> = _messages.value ?: ArrayList()

                val messagesMap: HashMap<String, String> =
                    snapshot.value as? HashMap<String, String> ?: return

                val message: MessageReceived =
                    MessageReceived(
                        id = snapshot.key!!,
                        name = messagesMap["name"]!!,
                        message = messagesMap["message"]!!
                    )

                if (currentList.contains(message))
                    currentList.remove(message)

                _messages.value = currentList
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

//        messageRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                val currentMessageList : ArrayList<Message> = ArrayList()
//
//                val messagesMap : HashMap<String, HashMap<String, String>> =
//                    snapshot.value as? HashMap<String, HashMap<String, String>> ?: return
//                messagesMap.map { messageMap ->
//                    val message : Message =
//                        Message(
//                            name = messageMap.value["name"]!!,
//                            message = messageMap.value["message"]!!
//                        )
//
//                    currentMessageList.add(message)
//                }
//                _messages.value = currentMessageList
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })

    }

    fun uploadMessageOnDB(message: MessageSend) {
        val messageRef = firebaseDatabase.getReference("messages").push()
        messageRef.setValue(message)

        _isMessageSent.value = true
    }

    fun deleteMessageFromFirebase(messageId: String) {
        val messageRef = firebaseDatabase.getReference("messages")
        messageRef.child(messageId).removeValue()
    }


    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessageViewModel() as T
        }
    }
}