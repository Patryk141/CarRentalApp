package com.example.carrental

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthSignInClient(
  private val context : Context,
  private val oneTapClient: SignInClient
) {

  private val auth = Firebase.auth

  suspend fun signIn() : IntentSender? {
    val result = try {
      oneTapClient.beginSignIn(
        buildSignInRequest()
      ).await()
    } catch (e : Exception) {
      e.printStackTrace()
      if(e is CancellationException) throw e
      null
    }

    return result?.pendingIntent?.intentSender
  }

  suspend fun signInWithIntent(intent: Intent) : SignInResult {
    val credential = oneTapClient.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    return try {
      val user = auth.signInWithCredential(googleCredentials).await().user
      SignInResult(
        data = user?.run {
          UserData(
            userId = uid,
            username = displayName,
            profilePicture = photoUrl?.toString()
          )
        },
        errorMessage = null
      )
    } catch (e : Exception) {
      e.printStackTrace()
      if(e is CancellationException) throw e
      SignInResult(
        data = null,
        errorMessage = e.message
      )
    }
  }

  fun getUser() : UserData? = auth.currentUser?.run {
    UserData(
      userId = uid,
      username = displayName,
      profilePicture = photoUrl?.toString()
    )
  }

  suspend fun signOut() {

  }

  private fun buildSignInRequest(): BeginSignInRequest {
    return BeginSignInRequest.Builder()
      .setGoogleIdTokenRequestOptions(
        GoogleIdTokenRequestOptions.builder()
          .setSupported(true)
          .setFilterByAuthorizedAccounts(false)
          .setServerClientId(context.getString(R.string.web_client_id)) // server client id
          .build()
      )
      .setAutoSelectEnabled(true)
      .build()
  }

}