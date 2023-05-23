package com.example.carrental

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var carsList : List<Car> = emptyList()
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment())
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val currentUser = FirebaseAuthSingleton.getInstance().currentUser
        val userEmail: String? = currentUser!!.email
        val photoUrl: Uri? = currentUser.photoUrl
        val userName: String? = currentUser.displayName

        db = Firebase.firestore
        val storageRef = FirebaseStorage.getInstance().reference.child("images/")

        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val imagePaths = mutableListOf<String>()

                for (item in listResult.items) {
                    val path = item.path
                    imagePaths.add(path)
                }

                println("Lista ścieżek do zdjęć: $imagePaths")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Błąd pobierania listy elementów z Firebase Storage", exception)
            }
//
//        val apiClient = CarApiClient().create()
////        val call = apiClient.getCarsData(1, "yes", 2020)
//        GlobalScope.launch(Dispatchers.IO) {
//            val response = apiClient.getCarsData(1, "yes", 2020).awaitResponse()
//            if(response.isSuccessful) {
//                carsList = response.body()!!.data
//                println(carsList!!.size)
//                println("Cars data[0]: ${carsList[0]}")
//                insertCarData()
//            } else {
//                // Obsłużenie błędów
//            }
////            val call = apiClient.getCarsData(1, "yes", 2020)
//        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.favorite -> {
                    replaceFragment(FavoriteFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.account -> {
                    replaceFragment(AccountFragment.newInstance(userName, userEmail, photoUrl))
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun insertCarData() {
        carsList.forEach {car ->
            val cost = Random.nextInt(100, 1000)
            val carData = hashMapOf(
                "id" to car.id,
                "brand" to car.make.name,
                "model" to car.name,
                "cost" to cost,
            )
            // Add a new document with a generated ID
            db.collection("carsData")
                .add(carData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    private fun AppCompatActivity.replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}