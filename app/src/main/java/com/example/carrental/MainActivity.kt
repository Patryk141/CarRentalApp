package com.example.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageUrls : ArrayList<String> = intent.extras?.getStringArrayList("1234") as ArrayList<String>
        Log.e("", imageUrls[0])
        replaceFragment(HomeFragment(imageUrls))
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment(imageUrls))
                    return@setOnItemSelectedListener true
                }
                R.id.favorite -> {
                    replaceFragment(FavoriteFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.account -> {
                    replaceFragment(AccountFragment())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun AppCompatActivity.replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}