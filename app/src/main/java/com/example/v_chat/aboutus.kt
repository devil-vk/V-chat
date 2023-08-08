package com.example.v_chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.v_chat.databinding.ActivityAboutusBinding

class aboutus : AppCompatActivity() {
    private lateinit var binding : ActivityAboutusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAboutusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            var intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}