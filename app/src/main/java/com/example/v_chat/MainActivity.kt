package com.example.v_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.v_chat.Adaptor.FragmentAdapter
import com.example.v_chat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var inflater= menuInflater
        inflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings->{
                var intent2=Intent(this,SettingsActivity::class.java)
                startActivity(intent2)
            }
            R.id.groupchat->{
                Toast.makeText(this,"Group chat started",Toast.LENGTH_SHORT).show()
                var intent1= Intent(this,GroupChatActivity::class.java)
                startActivity(intent1)
            }
            R.id.Logout->{
                mAuth.signOut()
                var intent = Intent(this,SigninActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}