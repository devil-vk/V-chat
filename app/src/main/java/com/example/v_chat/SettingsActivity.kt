package com.example.v_chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.v_chat.databinding.ActivitySettingsBinding
import com.example.v_chat.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private lateinit var reference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        supportActionBar?.hide()

        binding.backArrow.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.saveButton.setOnClickListener {
            if (binding.etStatus.text.toString().equals("") || binding.txtUsername.text.toString().equals("")){
                Toast.makeText(this,"Please enter valid details",Toast.LENGTH_SHORT).show()

            }
            else {
                val status = binding.etStatus.text.toString()
                val username = binding.txtUsername.text.toString()

                val obj = mutableMapOf<String, Any>()
                obj["username"] = username
                obj["status"] = status

                database.reference.child("Users").child(FirebaseAuth.getInstance().uid.toString())
                    .updateChildren(obj)

                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            }
        }


        database.reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(users::class.java)
                    Picasso.get()
                        .load(users?.profilepic)
                        .placeholder(R.drawable.avatar)
                        .into(binding.profileimage)

                    binding.txtUsername.setText(users?.username)
                    binding.etStatus.setText(users?.status)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })


        binding.textview1.setOnClickListener {
            var intent = Intent(this,aboutus::class.java)
            startActivity(intent)
        }
        binding.plus.setOnClickListener {
            var intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,27)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if (data?.data != null) {
             val sFile: Uri = data.data!!
             binding.profileimage.setImageURI(sFile)

             reference = storage.reference.child("profile_pic")
                 .child(FirebaseAuth.getInstance().uid.toString())

             reference.putFile(sFile)
                 .addOnSuccessListener {
                     reference.downloadUrl
                         .addOnSuccessListener { uri ->
                             database.reference
                                 .child("Users")
                                 .child(FirebaseAuth.getInstance().uid.toString())
                                 .child("profilepic")
                                 .setValue(uri.toString())
                         }
                 }
         }
    }
}