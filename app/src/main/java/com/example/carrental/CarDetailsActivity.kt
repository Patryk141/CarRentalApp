package com.example.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView

class CarDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardetails)
        val imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
        imageView.setImageResource(R.drawable.audi)
    }
}