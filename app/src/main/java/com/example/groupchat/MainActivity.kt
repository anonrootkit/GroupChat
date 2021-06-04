package com.example.groupchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.groupchat.databinding.ActivityMainBinding
import com.example.groupchat.model.Message
import com.example.groupchat.ui.MessageAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}