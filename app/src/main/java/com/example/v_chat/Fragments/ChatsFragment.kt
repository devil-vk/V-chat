package com.example.v_chat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.v_chat.Adaptor.UsersAdapter
import com.example.v_chat.R
import com.example.v_chat.databinding.FragmentChatsBinding
import com.example.v_chat.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentChatsBinding.inflate(inflater, container, false)
        val database = FirebaseDatabase.getInstance()
        val userList = ArrayList<users>() // Replace User with the appropriate class for your user model
        val adapter = UsersAdapter(userList, requireContext())
        binding.chatRecyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerview.layoutManager = layoutManager
        database.reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapshot in snapshot.children) {
                    val users = dataSnapshot.getValue(users::class.java)
                    users?.userid = dataSnapshot.key
                    if (users?.userid != FirebaseAuth.getInstance().uid) {
                        if (users != null) {
                            userList.add(users)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })


        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}