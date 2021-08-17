package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tmdcontactsapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        val slideAnimation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.side_slide)
        binding.loadingLogo.startAnimation(slideAnimation)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

        binding.splashScreen.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setContentView(binding.root)
    }
}