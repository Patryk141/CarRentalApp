package com.example.carrental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

  private lateinit var textInputLayoutEmail : TextInputLayout
  private lateinit var textInputLayName : TextInputLayout
  private lateinit var textInputLayPassword : TextInputLayout
  private lateinit var textInputLayPhone : TextInputLayout
  private lateinit var registerBtn : Button
  private lateinit var progressBar: ProgressBar
  private lateinit var alreadyregisteredBtn : Button
  private lateinit var editTextEmail  : TextInputEditText
  private lateinit var editTextName : TextInputEditText
  private lateinit var editTextPhone : TextInputEditText
  private lateinit var editTextPassword : TextInputEditText
  private lateinit var auth: FirebaseAuth

  public override fun onStart() {
    super.onStart()
    // Check if user is signed in (non-null) and update UI accordingly.
    auth.signOut()
    val currentUser = FirebaseAuthSingleton.getInstance().currentUser
    if (currentUser != null) {
      Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    auth = Firebase.auth

    registerBtn = findViewById(R.id.registerBtn)
    alreadyregisteredBtn = findViewById(R.id.btnAccAlready)
    progressBar = findViewById(R.id.progressBar)
    textInputLayoutEmail = findViewById(R.id.textInputLayEmail)
    textInputLayName = findViewById(R.id.textInputLayName)
    textInputLayPassword = findViewById(R.id.textInputLayPassword)
    textInputLayPhone = findViewById(R.id.textInputLayPhone)

    editTextEmail = findViewById(R.id.editTextEmail)
    editTextName = findViewById(R.id.editTextName)
    editTextPhone = findViewById(R.id.editTextPhone)
    editTextPassword = findViewById(R.id.editTextPassword)

    alreadyregisteredBtn.setOnClickListener{
      moveToLoginPage()
    }
  }


  private fun validateInputs() : Boolean {
    var areInputsValid = true
    var name = editTextName.text
    val email = editTextEmail.text
    val password = editTextPassword.text
    val phoneNum = editTextPhone.text

    if(password == null || password.length < 6) {
      textInputLayPassword.error = "Invalid password(min 6 sybmols)"
      areInputsValid = false
    }

    if(phoneNum!!.length < 9 || phoneNum == null) {
      textInputLayPhone.error = "Invalid phone number(9 numbers)"
      areInputsValid = false
    }

    if(name == null || name.length < 1) {
      textInputLayName.error = "Invalid field"
      areInputsValid = false
    }
    if(email == null || !email.contains("@")) {
      textInputLayoutEmail.error = "Invalid email"
      areInputsValid = false
    }

    return areInputsValid
  }

  fun onRegister(view: View) {
    progressBar.visibility = View.VISIBLE
    var validInputs = validateInputs()
    if(!validInputs) progressBar.visibility = View.GONE

    var userName = editTextName.text.toString()
    var email = editTextEmail.text.toString()
    var password = editTextPassword.text.toString()
    var phoneNum = editTextPhone.text.toString()

    if(validInputs) {
      // Code to register the user in Firebase
      auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
          progressBar.visibility = View.GONE
          if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            var user = auth.currentUser

//            user!!.updatePhoneNumber(phoneNum) - to do (phoneNum verification)
            user!!.updateEmail(email).addOnCompleteListener { emailTask ->
              if(emailTask.isSuccessful) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                  .setDisplayName(userName)
                  .build()
                user!!.updateProfile(profileUpdates)
                  .addOnCompleteListener{ profileTask ->
                    if(profileTask.isSuccessful) {
                      Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                      moveToLoginPage()
                    }
                  }
              }
            }
          } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(
              baseContext,
              "Authentication failed.",
              Toast.LENGTH_SHORT,
            ).show()
          }
        }
    } else {
      Toast.makeText(this, "Inputs are not valid", Toast.LENGTH_SHORT).show()
    }
  }

  private fun moveToLoginPage() {
    val intent = Intent(this, LoginActivity::class.java)
    intent.putExtra("registered", "login after registration")
    startActivity(intent)
    finish()
  }


}