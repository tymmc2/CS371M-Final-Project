package com.example.stockapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityRegisterBinding
import com.example.stockapp.databinding.ActivitySigninBinding
import com.example.stockapp.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.register.setOnClickListener { register() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun register() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        val confirm = binding.confirmPassword.text.toString()
        if (password != confirm) {
            Toast.makeText(applicationContext, "Passwords do not match!", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Account created!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        print("Logged in with user ${auth.currentUser}")
                    } else {
                        Toast.makeText(applicationContext, "Failed to login! " + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}