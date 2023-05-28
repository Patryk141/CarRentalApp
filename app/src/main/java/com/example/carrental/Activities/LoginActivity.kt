package com.example.carrental.Activities

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
import com.example.carrental.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
  private lateinit var googleSignInManager : GoogleSignInManager
  private lateinit var googleSignInLauncher : ActivityResultLauncher<Intent>

  public override fun onStart() {
    super.onStart()
    // Check if user is signed in (non-null) and update UI accordingly.
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    editTextEmail = findViewById(R.id.editTextEmail)
    editTextPassword = findViewById(R.id.editTextPassword)
    editTextPasswordRepeat = findViewById(R.id.editTextPasswordRepeat)

    var currentUser = FirebaseAuthSingleton.getInstance().currentUser
    val msg = intent.getStringExtra("register")

    if(msg == "login after registration") {
      currentUser = null
    }
    else if (currentUser != null) {
      Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show()
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
      finish()
    }

    loginBtn = findViewById(R.id.loginBtn)
    loginWithGoogleBtn = findViewById(R.id.sign_in_button)
    progressBar = findViewById(R.id.progressBar)

    auth = FirebaseAuthSingleton.getInstance()
//    val activityResultRegistry = this@LoginActivity.activityResultRegistry
    val googleSignInClient: GoogleSignInClient by lazy { // wartość tworzona w momencie pierwszego odwołania do niej
      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(this.getString(R.string.default_web_client_id))
        .requestId()
        .requestEmail()
        .build()
      GoogleSignIn.getClient(this, gso)
    }

//    googleSignInManager = GoogleSignInManager(this, auth, activityResultRegistry)
    googleSignInManager = GoogleSignInManager(this@LoginActivity, auth, activityResultRegistry)

    loginWithGoogleBtn.setOnClickListener {
      val signInIntent = googleSignInClient.signInIntent
      startActivityForResult(signInIntent, 100)
//      googleSignInLauncher.launch(signInIntent)
//      googleSignInManager.signInWithGoogle()
    }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == 100) {
      val signInAccountTask: Task<GoogleSignInAccount> =
        GoogleSignIn.getSignedInAccountFromIntent(data)

      if (signInAccountTask.isSuccessful) {
        try {
          val googleSignInAccount = signInAccountTask.getResult(ApiException::class.java)
          if(googleSignInAccount != null) {
            // When sign in account is not equal to null initialize auth credential
            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
              googleSignInAccount.idToken, null
            )
            auth.signInWithCredential(authCredential)
              .addOnCompleteListener {
                if(it.isSuccessful) {
                  startActivity(
                    Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  )
                  Toast.makeText(this, "Firebase authentication successful", Toast.LENGTH_SHORT).show()
                } else {
                  Toast.makeText(this, "Firebase authentication failed: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
              }
          }
        }
        catch(e : ApiException) {
          e.printStackTrace()
        }
      }
    }
  }


  private fun validateInputs(email: String, password: String, passwordRepeat: String) : Boolean {
    if(!email.contains("@") || password.length < 4 || passwordRepeat.length < 4) {
      Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show()
      return false
    }
    if(!password.equals(passwordRepeat) || password == null || passwordRepeat == null) {
      Toast.makeText(this, "Problem with log in", Toast.LENGTH_SHORT).show()
      return false
    }

    return true
  }

  fun onLogin(view: View) {
    progressBar.visibility = View.VISIBLE
    val email = editTextEmail.text.toString()
    val password = editTextPassword.text.toString()
    val passwordRepeat = editTextPasswordRepeat.text.toString()
    val result = validateInputs(email, password, passwordRepeat)

    if (!result) {
      progressBar.visibility = View.GONE
    } else {
      auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
          progressBar.visibility = View.GONE
          if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            val user = auth.currentUser
            Toast.makeText(this, "User signed in successfully", Toast.LENGTH_SHORT).show()
            // new intent to home activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

  fun launchRegister(view: View) {
    val intent = Intent(this, RegisterActivity::class.java)
    startActivity(intent)
    finish()
  }
}