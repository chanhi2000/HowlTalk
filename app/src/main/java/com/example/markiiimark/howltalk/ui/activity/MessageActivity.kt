package com.example.markiiimark.howltalk.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageActivity : AppCompatActivity() {
    private val messageEditText by lazy {  findViewById<EditText>(R.id.messageEditText)}

    private var uid:String? = null
    private var destinationUid:String? = null
    private var chatroomUid:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        uid = FirebaseAuth.getInstance().currentUser?.uid
        destinationUid = intent.extras.getString("destinationUid")
        checkChatroom()
    }

    fun sendButtonClicked(v: View) {
        val chatModel = ChatModel().apply {
            users.put(uid!!, true)
            users.put(destinationUid!!, true)
            // TODO: desintationUid from intent is null. Fix it
        }
        if (chatroomUid.isNullOrBlank()) {
            FirebaseDatabase.getInstance().reference.child("chatrooms").push().setValue(chatModel)
        } else {
            val comment = ChatModel.Companion.Comment(uid, messageEditText.text.toString())
            FirebaseDatabase.getInstance().reference.child("chatrooms").child(chatroomUid).child("commnets").push().setValue(comment)
        }
        checkChatroom()
    }

    private fun checkChatroom() {
        FirebaseDatabase.getInstance().reference
                .child("chatrooms").orderByChild("users/${uid}").equalTo(true)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        dataSnapshot!!.children.forEach { item ->
                            val model = item.getValue(ChatModel::class.java)
                            if (model!!.users.containsKey(destinationUid)) {
                                chatroomUid = item.key
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                    }
                })
    }
}