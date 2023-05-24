package com.example.carrental

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {

  private lateinit var db : FirebaseFirestore
  private lateinit var imageView: AppCompatImageView
  private lateinit var car : CarData
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cardetails)
    imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
    val carID: String? = intent.getStringExtra("ID")
    db = Firebase.firestore

    GlobalScope.launch(Dispatchers.IO) {
      if (carID != null) {
        db.collection("carsData")
          .document(carID)
          .get()
          .addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
              car = documentSnapshot.toObject(CarData::class.java)!!
              handleCarData(car)
            }
          }
          .addOnFailureListener{ ex ->
            Log.e(ContentValues.TAG, "Error getting cars data: ", ex)
          }
      }
    }

//    imageView.setImageResource(R.drawable.audi)
  }

  private fun handleCarData(car: CarData) {
    val storageRef = FirebaseStorage.getInstance().getReference().child(car!!.url)
    storageRef.downloadUrl.addOnSuccessListener { uri ->
      val imageUrl = uri.toString()

      Glide.with(this)
        .load(imageUrl)
        .override(750, 750)
        .into(imageView)
    }
      .addOnFailureListener { exception ->
        // Obsługa błędu
        Log.e(ContentValues.TAG, "Error getting download URL: $exception")
      }
  }

}