package com.example.carrental.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.carrental.FirebaseAuthSingleton
import com.example.carrental.R
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

    val fAuth = FirebaseAuthSingleton.getInstance()
//    fAuth.signOut()
    appNameTxt = findViewById<TextView>(R.id.appName);

    CoroutineScope(Dispatchers.Main).launch {// wykonanie kodu na wątku głównym
      delay(splashTimeout)
//      intent.putExtra("1234", imageUrls)
      if(fAuth.currentUser == null) {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
      }
      else {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
      }
    }

    val myAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_anim_1)
    appNameTxt.startAnimation(myAnimation)
  }
}