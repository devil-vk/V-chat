package com.example.v_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splashscreen : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}