package com.example.carrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SplashScreenActivity : AppCompatActivity() {

  lateinit var appNameTxt : TextView
//  private val splashTimeout = 1750L

  private val imageRef = Firebase.storage.reference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    appNameTxt = findViewById<TextView>(R.id.appName);

    CoroutineScope(Dispatchers.Main).launch {// wykonanie kodu na wątku głównym
//      delay(splashTimeout)
      val imageUrls = ArrayList<String>()
      val images = imageRef.child("images/").listAll().await()
      for (image in images.items) {
        val url = image.downloadUrl.await()
        imageUrls.add(url.toString())
      }
//      val intent = Intent(this@SplashScreenActivity, RegisterActivity::class.java)
      val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
      intent.putExtra("1234", imageUrls)
      startActivity(intent)
      finish()
    }

    val myAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_anim_1)
    appNameTxt.startAnimation(myAnimation)
  }
}