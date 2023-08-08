package com.example.v_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.v_chat.Adaptor.ChatAdapter
import com.example.v_chat.databinding.ActivityGroupChatBinding
import com.example.v_chat.models.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.Date

class GroupChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var messageModels: ArrayList<MessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        database = FirebaseDatabase.getInstance()
        messageModels = ArrayList()

        val senderId = FirebaseAuth.getInstance().uid.toString()
        binding.userName.text = "Group Chat"

        val adapter = ChatAdapter(messageModels,this)
        binding.chatrecyclerview.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.chatrecyclerview.layoutManager=layoutManager

        database.reference.child("Group Chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageModels.clear()
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue(MessageModel::class.java)
                        if (model != null) {
                            messageModels.add(model)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        binding.send.setOnClickListener {
            val message = binding.Entermessage.text.toString()
            val model = MessageModel(senderId,message)
            model.timestamp=Date().time

            binding.Entermessage.setText("")
            database.reference.child("Group Chats")
                .push()
                .setValue(model)
                .addOnSuccessListener {
                    Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show()
                }
        }

    }
}