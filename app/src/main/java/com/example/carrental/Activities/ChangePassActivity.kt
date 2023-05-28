package com.example.carrental.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.carrental.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassActivity : AppCompatActivity() {
    private lateinit var editTextPassword : TextInputEditText
    private lateinit var editTextNewPassword : TextInputEditText
    private lateinit var editTextNewPasswordRepeat : TextInputEditText
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        auth = FirebaseAuth.getInstance()
        editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword1)
        editTextNewPassword = findViewById<TextInputEditText>(R.id.editTextInputNewPassword)
        editTextNewPasswordRepeat = findViewById<TextInputEditText>(R.id.editTextInputRepeatNewPassword)
    }

    fun changePass(view: View) {
        if (editTextPassword.text!!.isNotEmpty() && editTextNewPassword.text!!.isNotEmpty() && editTextNewPasswordRepeat.text!!.isNotEmpty()) {
            if (editTextNewPassword.text.toString() == editTextNewPasswordRepeat.text.toString()) {
                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, editTextPassword.text.toString())
                    user?.reauthenticate(credential)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT).show()

                            user?.updatePassword(editTextNewPassword.text.toString())?.addOnSuccessListener {
                                Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                        } else {
                            Toast.makeText(this, "Re-Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        } else {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}