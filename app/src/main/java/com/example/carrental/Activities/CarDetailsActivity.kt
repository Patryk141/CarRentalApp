package com.example.carrental.Activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carrental.Adapters.CarDetailsAdapter
import com.example.carrental.CarFeature
import com.example.carrental.Data.CarData
import com.example.carrental.R
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
  private lateinit var carFeatures : RecyclerView
  private lateinit var nameCar : TextView
  private var imagePath : String? = ""
  private lateinit var car : CarData

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cardetails)

    var carFeaturesList : ArrayList<CarFeature> = ArrayList()
    var carPrice = findViewById<TextView>(R.id.textViewPrice)
    imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
    carFeatures = findViewById(R.id.carFeaturesRecycler)
    carFeatures.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    nameCar = findViewById<TextView>(R.id.textView6)

    val carID: String? = intent.getStringExtra("ID")
    imagePath = intent.getStringExtra("imagePath")
    db = Firebase.firestore
    val carFeaturesCollection = db.collection("carsFeatures")

    GlobalScope.launch(Dispatchers.IO) {
      if (carID != null) {
        db.collection("carsData")
          .document(carID)
          .get()
          .addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
              car = documentSnapshot.toObject(CarData::class.java)!!
              carPrice.text = car.cost.toString() + "$/day"
              nameCar.text = car.brand + " " + car.model
              handleCarData(imagePath)

              carFeaturesCollection
                .document(carID)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                  val features = documentSnapshot.get("features") as? List<HashMap<String, Any>>
                  features!!.forEach { feature ->
                    val name = feature["name"] as? String
                    val value = feature["value"]
                    if(name != null && value != null) {
                      val carFeature = CarFeature(name, value)
                      carFeaturesList.add(carFeature)
                    }
                  }
                  carFeatures.adapter = CarDetailsAdapter(carFeaturesList)
                }
            }
          }
          .addOnFailureListener{ ex ->
            Log.e(ContentValues.TAG, "Error getting cars data: ", ex)
          }
      }
    }

  }

  private fun handleCarData(imageUrl: String?) {
    Glide.with(this)
        .load(imageUrl)
        .override(700, 700)
        .into(imageView)
  }

  fun goToCart(view: View) {
    val intent = Intent(this, CartActivity::class.java)
    intent.putExtra("imagePath", imagePath)
    intent.putExtra("cost", car.cost.toString())
    intent.putExtra("model", car.brand + " " + car.model)
    startActivity(intent)
  }

}