package com.example.stockapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivitySearchBinding
import com.example.stockapp.databinding.ActivitySigninBinding
import com.example.stockapp.home.HomeActivity
import com.example.stockapp.search.SearchActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener{ login() }

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            it.context.startActivity(intent)
        }

        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Failed to login! User Name or Password is Empty", Toast.LENGTH_SHORT).show()
            return;
        }
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Failed to login! " + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}