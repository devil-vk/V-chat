package com.example.v_chat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.v_chat.databinding.ActivitySigninBinding
import com.example.v_chat.models.users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    companion object {
        private const val RC_SIGN_IN = 65
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        supportActionBar?.hide()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("please wait\nValidating Credentials")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnSignin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            if (!binding.txtEmail.text.toString().isEmpty() && !binding.txtPassword.text.toString().isEmpty()){
                progressDialog.show()
                mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {task ->
                        progressDialog.dismiss()
                        if (task.isSuccessful){
                            var intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

            }
            else{
                Toast.makeText(this,"Enter Credentials",Toast.LENGTH_SHORT).show()
            }
        }
        if (mAuth.currentUser!=null){
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.txtClickSignup.setOnClickListener {
            var intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val RC_SIGN_IN =65
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d("","Firebase auth with Google"+account.id)
                    firebaseAuthWithGoogle(account.idToken)
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign-in failed", e)
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    val Users = users().apply {
                        userid = user?.uid
                        username = user?.displayName
                        profilepic = user?.photoUrl.toString()
                    }
                    if (user != null) {
                        database.reference.child("Users").child(user.uid).setValue(Users)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Sign-in successful with Google",Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("FirebaseAuth", "Firebase sign-in failed", task.exception)
                    Toast.makeText(this, "Firebase sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}