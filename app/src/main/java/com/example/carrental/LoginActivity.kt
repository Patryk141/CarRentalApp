package com.example.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

  lateinit var loginBtn : Button
  lateinit var loginWithGoogleBtn : SignInButton
  lateinit var editTextName : TextInputEditText
  lateinit var editTextPassword : TextInputEditText
  lateinit var editTextPasswordRepeat : TextInputEditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    loginBtn = findViewById(R.id.loginBtn)
    loginWithGoogleBtn = findViewById(R.id.sign_in_button)

    editTextName = findViewById(R.id.editTextName)
    editTextPassword = findViewById(R.id.editTextPassword)
    editTextPasswordRepeat = findViewById(R.id.editTextPasswordRepeat)

    loginBtn.setOnClickListener{
      Toast.makeText(this, "You attempt to log in", Toast.LENGTH_SHORT).show()
      val name = editTextName.text
      val password = editTextPassword.text
      val passwordRepeat = editTextPasswordRepeat.text

      if(password != passwordRepeat || password == null || passwordRepeat == null) {
        Toast.makeText(this, "Problem with log in", Toast.LENGTH_SHORT).show()
      }

    }

  }
}