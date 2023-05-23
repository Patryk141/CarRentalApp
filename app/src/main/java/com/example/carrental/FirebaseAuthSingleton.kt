package com.example.carrental

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthSingleton {
  private var instance: FirebaseAuth? = null

  fun getInstance(): FirebaseAuth {
    synchronized(this) {
      if (instance == null) {
        instance = FirebaseAuth.getInstance()
      }
      return instance!! // not null
    }
  }

  fun resetInstance() {
    instance = null
  }

}