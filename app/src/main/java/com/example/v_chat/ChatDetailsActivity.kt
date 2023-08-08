package com.example.v_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.v_chat.Adaptor.ChatAdapter
import com.example.v_chat.databinding.ActivityChatDetailsBinding
import com.example.v_chat.models.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ChatDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageModels: ArrayList<MessageModel>
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        senderId = auth.uid.toString()
        receiverId = intent.getStringExtra("userid").toString()
        val userName = intent.getStringExtra("username")
        val profilePic = intent.getStringExtra("profilepic")

        binding.userName.text = userName
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profilePic)

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        messageModels = ArrayList()
        chatAdapter = ChatAdapter(messageModels, this, receiverId)
        binding.chatrecyclerview.adapter = chatAdapter
        binding.chatrecyclerview.layoutManager = LinearLayoutManager(this)

        senderRoom = "$senderId+$receiverId"
        receiverRoom = "$receiverId+$senderId"

        val chatsRef = database.reference.child("chats")

        chatsRef.child(senderRoom)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val model = snapshot.getValue(MessageModel::class.java)
                    model?.let {
                        messageModels.add(it)
                        chatAdapter.notifyDataSetChanged()
                        binding.chatrecyclerview.scrollToPosition(messageModels.size - 1)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle changed event
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Handle removed event
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle moved event
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })

        binding.send.setOnClickListener {
            val message = binding.Entermessage.text.toString().trim()
            if (message.isNotEmpty()) {
                val model = MessageModel(senderId, message)
                val messageId = database.reference.child("chats").push().key
                if (messageId != null) {
                    model.messageID = messageId
                    database.reference.child("chats").child(senderRoom).child(messageId).setValue(model)
                    database.reference.child("chats").child(receiverRoom).child(messageId).setValue(model)
                }
                binding.Entermessage.text.clear()
            }
        }

    }
}
