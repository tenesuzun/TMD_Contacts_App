package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.HandlerCompat
import com.example.tmdcontactsapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        val slideAnimation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.side_slide)
        binding.loadingLogo.startAnimation(slideAnimation)

        val handler = Handler()

        val runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        handler.postDelayed(runnable, 3000)

        binding.splashScreen.setOnClickListener{
            handler.removeCallbacks(runnable)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContentView(binding.root)
    }
}