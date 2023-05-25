package com.example.carrental

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarDetailsActivity : TransformationAppCompatActivity() {

  private lateinit var db : FirebaseFirestore
  private lateinit var imageView: AppCompatImageView
  private lateinit var nameCar : TextView
  private lateinit var car : CarData
  private lateinit var carID : String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cardetails)

    imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
    nameCar = findViewById<TextView>(R.id.textView6)
    db = Firebase.firestore

    val url: String? = intent.extras?.getString("url")
    carID = intent.getStringExtra("ID").toString()

    Glide.with(this).load(url).into(imageView)

    GlobalScope.launch(Dispatchers.IO) {
      if (carID != null) {
        db.collection("carsData")
          .document(carID!!)
          .get()
          .addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
              car = documentSnapshot.toObject(CarData::class.java)!!
              nameCar.text = car.brand + " " + car.model
//              handleCarData(car)
            }
          }
          .addOnFailureListener{ ex ->
            Log.e(ContentValues.TAG, "Error getting cars data: ", ex)
          }
      }
    }

//    imageView.setImageResource(R.drawable.audi)
  }

//  private fun handleCarData(car: CarData) {
//    val storageRef = FirebaseStorage.getInstance().reference.child(car!!.url)
//    nameCar.text = car.brand + " " + car.model
//    storageRef.downloadUrl.addOnSuccessListener { uri ->
//      val imageUrl = uri.toString()
//
//      Glide.with(this)
//        .load(imageUrl)
////        .override(750, 750)
//        .into(imageView)
//    }
//      .addOnFailureListener { exception ->
//        // Obsługa błędu
//        Log.e(ContentValues.TAG, "Error getting download URL: $exception")
//      }
//  }

}