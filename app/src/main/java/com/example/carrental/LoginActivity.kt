package com.example.carrental

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

  private lateinit var loginBtn : Button
  private lateinit var loginWithGoogleBtn : SignInButton
  private lateinit var editTextEmail : TextInputEditText
  private lateinit var editTextPassword : TextInputEditText
  private lateinit var editTextPasswordRepeat : TextInputEditText
  private lateinit var progressBar : ProgressBar
  private lateinit var auth: FirebaseAuth
  private lateinit var googleSignInClient : GoogleSignInClient
  private lateinit var googleSignInLauncher : ActivityResultLauncher<Intent>

  public override fun onStart() {
    super.onStart()
    // Check if user is signed in (non-null) and update UI accordingly.
    auth.signOut()
    val currentUser = auth.currentUser
    if (currentUser != null) {
      Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show()
    }

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    loginBtn = findViewById(R.id.loginBtn)
    loginWithGoogleBtn = findViewById(R.id.sign_in_button)

    auth = FirebaseAuth.getInstance()

    val gso = GoogleSignInOptions.Builder(
      GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()

    googleSignInClient = GoogleSignIn.getClient(this, gso)

    googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
      if(result.resultCode == Activity.RESULT_OK) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleResult(task)
      }
    }

    loginWithGoogleBtn.setOnClickListener {
      val signInIntent = googleSignInClient.signInIntent
      googleSignInLauncher.launch(signInIntent)
    }

    editTextEmail = findViewById(R.id.editTextEmail)
    editTextPassword = findViewById(R.id.editTextPassword)
    editTextPasswordRepeat = findViewById(R.id.editTextPasswordRepeat)
  }

  private fun handleResult(task: Task<GoogleSignInAccount>) {
    if(task.isSuccessful) {
      val account : GoogleSignInAccount? = task.result
      if(account != null) {
        updateUI(account)
      }
    } else {
      Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
    }
  }

  private fun updateUI(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    auth.signInWithCredential(credential).addOnCompleteListener {
      if(it.isSuccessful) {
        val intent : Intent = Intent(this, HomeActivity::class.java) //
        intent.putExtra("email", account.email)
        intent.putExtra("name", account.displayName)
        intent.putExtra("photo", account.photoUrl)
        startActivity(intent)
      } else {
        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
      }
    }
  }

  fun onLogin(view: View) {
//    progressBar.visibility = View.VISIBLE
    val email = editTextEmail.text.toString()
    val password = editTextPassword.text.toString()
    val passwordRepeat = editTextPasswordRepeat.text.toString()


    if(!password.equals(passwordRepeat) || password == null || passwordRepeat == null) {
      Toast.makeText(this, "Problem with log in", Toast.LENGTH_SHORT).show()
      return
    }

    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
//        progressBar.visibility = View.GONE
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          val user = auth.currentUser
          Toast.makeText(this, "User signed in successfully", Toast.LENGTH_SHORT).show()
          val intent = Intent(this@LoginActivity, MainActivity::class.java)
          startActivity(intent)
          // new intent to home screen activity
        } else {
          // If sign in fails, display a message to the user.
          Toast.makeText(
            baseContext,
            "Authentication failed.",
            Toast.LENGTH_SHORT,
          ).show()
        }
      }
  }

}