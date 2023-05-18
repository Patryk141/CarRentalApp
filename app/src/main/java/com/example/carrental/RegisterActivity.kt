package com.example.carrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

  lateinit var textInputLayoutEmail : TextInputLayout
  lateinit var textInputLayName : TextInputLayout
  lateinit var textInputLayPassword : TextInputLayout
  lateinit var textInputLayPhone : TextInputLayout
  lateinit var registerBtn : Button
  lateinit var alreadyregisteredBtn : Button
  lateinit var editTextEmail  : TextInputEditText
  lateinit var editTextName : TextInputEditText
  lateinit var editTextPhone : TextInputEditText
  lateinit var editTextPassword : TextInputEditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    registerBtn = findViewById(R.id.registerBtn)
    alreadyregisteredBtn = findViewById(R.id.btnAccAlready)

    textInputLayoutEmail = findViewById(R.id.textInputLayEmail)
    textInputLayName = findViewById(R.id.textInputLayName)
    textInputLayPassword = findViewById(R.id.textInputLayPassword)
    textInputLayPhone = findViewById(R.id.textInputLayPhone)

    editTextEmail = findViewById(R.id.editTextEmail)
    editTextName = findViewById(R.id.editTextName)
    editTextPhone = findViewById(R.id.editTextPhone)
    editTextPassword = findViewById(R.id.editTextPassword)

    editTextEmail.doAfterTextChanged {
      println("${it.toString()}")
    }

    registerBtn.setOnClickListener{
      var name = editTextName.text
      val email = editTextEmail.text
      val password = editTextPassword.text
      val phoneNum = editTextPhone.text

      if(password == null || password.length < 4) {
        textInputLayPassword.error = "Invalid password(min 4 sybmols)"
      }

      if(phoneNum!!.length < 9 || phoneNum == null) {
        textInputLayPhone.error = "Invalid phone number(9 numbers)"
      }

      if(name == null) {
        textInputLayName.error = "Invalid field"
      }
      if(email == null || !email.contains("@")) {
        textInputLayoutEmail.error = "Invalid email"
      } else {
        Toast.makeText(this, "You are registered", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
      }
    }

    alreadyregisteredBtn.setOnClickListener{
      Toast.makeText(this, "You are already registered", Toast.LENGTH_SHORT).show()
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }


//    editTextEmail.doAfterTextChanged { text, start, before, count ->
//      if(!text!!.contains("@")) {
//        textInputLayoutEmail.error = "Invalid email"
//      } else {
//        textInputLayoutEmail.error = null
//      }
//    }

  }


}