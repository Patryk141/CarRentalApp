package com.example.carrental.Activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.Adapters.PaymentsAdapter
import com.example.carrental.Car
import com.example.carrental.CarData
import com.example.carrental.Data.Payment
import com.example.carrental.FirebaseAuthSingleton
import com.example.carrental.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaymentsActivity : AppCompatActivity() {

  private lateinit var paymentsRecyclerView : RecyclerView
  private lateinit var auth: FirebaseAuth
  private var paymentsList : List<Payment> = emptyList()
  private lateinit var db : FirebaseFirestore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_payments)
    db = Firebase.firestore
    paymentsRecyclerView = findViewById(R.id.recyclerViewPayments)
    paymentsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    getPaymentsForUser()
  }

  private fun getPaymentsForUser() {

    GlobalScope.launch(Dispatchers.IO) {
      db.collection("users")
        .document(FirebaseAuthSingleton.getInstance().currentUser!!.uid)
        .collection("payments")
        .get()
        .addOnSuccessListener { querySnapshot ->
          paymentsList = querySnapshot.documents.map { documentSnapshot ->
            var payment = documentSnapshot.toObject(Payment::class.java)
            payment!!
          }
          paymentsRecyclerView.adapter = PaymentsAdapter(paymentsList)
        }
        .addOnFailureListener {
          Log.e(ContentValues.TAG, "Error getting payments data: ", it)
        }
    }

  }

}