package com.example.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardetails)
        val imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
        val url = intent.extras?.getString("carImage")
        Glide.with(this).load(url).transition(DrawableTransitionOptions.withCrossFade()).into(imageView)
    }
}