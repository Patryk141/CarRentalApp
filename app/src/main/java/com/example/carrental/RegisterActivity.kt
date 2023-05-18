package com.example.carrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

  lateinit var textInputLayoutEmail : TextInputLayout
  lateinit var editTextEmail : TextInputEditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    textInputLayoutEmail = findViewById(R.id.textInputLayEmail)
    editTextEmail = findViewById(R.id.editTextEmail)

    editTextEmail.doAfterTextChanged {
      println("${it.toString()}")
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