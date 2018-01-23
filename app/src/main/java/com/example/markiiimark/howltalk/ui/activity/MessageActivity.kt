package com.example.markiiimark.howltalk.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageActivity : AppCompatActivity() {

    var destinationUid:String? = null
    private val sendButton by lazy {  findViewById<Button>(R.id.sendButton)  }
    private val messageEditText by lazy {  findViewById<EditText>(R.id.messageEditText)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        destinationUid = intent.getStringExtra("destinationUid")
    }

    fun sendButtonClicked(v: View) {
        val chatModel = ChatModel(FirebaseAuth.getInstance().currentUser?.uid, destinationUid)
        FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel)
    }
}