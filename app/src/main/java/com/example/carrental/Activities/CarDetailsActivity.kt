package com.example.carrental.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carrental.Adapters.CarDetailsAdapter
import com.example.carrental.CarData
import com.example.carrental.CarFeature
import com.example.carrental.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.random.Random

class CarDetailsActivity : AppCompatActivity() {

  private lateinit var db : FirebaseFirestore
  private lateinit var imageView: AppCompatImageView
  private lateinit var carFeatures : RecyclerView
  private var imagePath : String? = ""
  private lateinit var car : CarData

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cardetails)
    imageView = findViewById<AppCompatImageView>(R.id.appCompatImageView1)
    var carPrice = findViewById<TextView>(R.id.textViewPrice)
    carFeatures = findViewById(R.id.carFeaturesRecycler)
    carFeatures.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    var carFeaturesList : ArrayList<CarFeature> = ArrayList()
//    list.add(CarFeature("Gearbox", "Automatic"))
//    list.add(CarFeature("Acceleration", "4.2s 0-100 km/h"))
//    list.add(CarFeature("Engine Out", "400 HP"))
//    list.add(CarFeature("Engine In", "200 HP"))
//    carFeatures.adapter = CarDetailsAdapter(list)

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