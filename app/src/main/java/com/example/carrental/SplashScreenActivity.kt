package com.example.carrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

  lateinit var appNameTxt : TextView
  private val splashTimeout = 1750L

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    appNameTxt = findViewById<TextView>(R.id.appName);

    CoroutineScope(Dispatchers.Main).launch {// wykonanie kodu na wątku głównym
      delay(splashTimeout)
//      val intent = Intent(this@SplashScreenActivity, RegisterActivity::class.java)
      val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
      startActivity(intent)
      finish()
    }

    val myAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_anim_1)
    appNameTxt.startAnimation(myAnimation)
  }
}