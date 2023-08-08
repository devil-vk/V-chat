package com.example.v_chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.v_chat.databinding.ActivitySignupBinding
import com.example.v_chat.models.users
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.newFixedThreadPoolContext

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        supportActionBar?.hide()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creating Account")
        progressDialog.setMessage("We're creating your account.")

        binding.btnSignup.setOnClickListener {
            if (!binding.txtUsername.text.toString().isEmpty() && !binding.txtEmail.text.toString().isEmpty() && !binding.txtPassword.text.toString().isEmpty()){
                progressDialog.show()
                mAuth.createUserWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString())
                    .addOnCompleteListener { task ->
                        progressDialog.dismiss()
                        if (task.isSuccessful) {
                            val user = users(
                                binding.txtUsername.text.toString(),
                                binding.txtEmail.text.toString(),
                                binding.txtPassword.text.toString()
                            )
                            val id = task.result?.user?.uid

                            if (id != null) {
                                database.reference.child("Users").child(id).setValue(user)
                            }

                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val exception = task.exception
                            if (exception is FirebaseAuthWeakPasswordException) {
                                Toast.makeText(this, "Weak password. Please choose a stronger password.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

            }
            else{
                Toast.makeText(this,"Enter Credentials",Toast.LENGTH_SHORT).show()
            }
        }
        binding.txtAlreadyhaveaccount.setOnClickListener {
            var intent = Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }
    }
}