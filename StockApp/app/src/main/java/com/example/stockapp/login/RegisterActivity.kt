package com.example.stockapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityRegisterBinding
import com.example.stockapp.databinding.ActivitySigninBinding
import com.example.stockapp.home.HomeActivity
import com.github.infinitebanner.InfiniteBannerView
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var infiniteBanner : InfiniteBannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.register.setOnClickListener { register() }

        binding.registerBanner.adapter = Banner()
        binding.registerBanner.setPageTransformer { view, offset ->
            view?.scaleY = 0.8f + 0.2f * offset
            view?.alpha = 0.5f + 0.5f * offset
        }
        binding.registerBanner.onItemClickListener =
            InfiniteBannerView.OnItemClickListener { view, position ->  Toast.makeText(this, "Please Register Below", Toast.LENGTH_SHORT).show(); }
        binding.registerBanner.setInitPosition(1)

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
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(applicationContext, "Failed to create account! User Name or Password is Empty", Toast.LENGTH_SHORT).show()
            return;
        }
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