package com.example.carrental

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import com.example.carrental.Activities.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInManager(private val activity: Activity, private val auth: FirebaseAuth, private val activityResultRegistry: ActivityResultRegistry) {
  private val googleSignInClient: GoogleSignInClient by lazy { // wartość tworzona w momencie pierwszego odwołania do niej
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(activity.getString(R.string.default_web_client_id))
      .requestEmail()
      .build()

    GoogleSignIn.getClient(activity, gso)
  }

  private val googleSignInLauncher: ActivityResultLauncher<Intent> =
    activityResultRegistry.register("myCustomActivityResultKey", ActivityResultContracts.StartActivityForResult()) { result ->
      if(result.resultCode == Activity.RESULT_OK) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleResult(task)
      }
    }


  fun signInWithGoogle() {
    val signInIntent = googleSignInClient.signInIntent
    googleSignInLauncher.launch(signInIntent)
  }

  fun handleResult(task: Task<GoogleSignInAccount>) {
    if(task.isSuccessful) {
      val account : GoogleSignInAccount? = task.result
      if(account != null) {
        updateUI(account)
      }
    } else {
      Toast.makeText(activity, task.exception.toString(), Toast.LENGTH_SHORT).show()
    }
  }

  private fun updateUI(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    println(credential)
    auth.signInWithCredential(credential).addOnCompleteListener {
      if(it.isSuccessful) {
        val intent : Intent = Intent(activity, MainActivity::class.java)
        googleSignInLauncher.launch(intent)
//        activity.startActivity(intent)
      } else {
        Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
      }
    }
  }

}