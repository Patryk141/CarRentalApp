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
import com.google.firebase.firestore.ktx.toObject
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
    private lateinit var imagePaths : MutableList<String>
    private lateinit var carsListData: List<CarData?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Firebase.firestore

        GlobalScope.launch(Dispatchers.IO) {
            db.collection("carsData")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    carsListData = querySnapshot.documents.map { documentSnapshot ->
                        var carData = documentSnapshot.toObject(CarData::class.java)
                        carData
                    }
                    replaceFragment(HomeFragment(carsListData))
                }
                .addOnFailureListener { e ->
                    // Obsługa błędu
                    Log.e(TAG, "Error getting cars data: ", e)
                }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val currentUser = FirebaseAuthSingleton.getInstance().currentUser
        val userEmail: String? = currentUser!!.email
        val photoUrl: Uri? = currentUser.photoUrl
        val userName: String? = currentUser.displayName

        val storageRef = FirebaseStorage.getInstance().reference.child("images/")
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                imagePaths = mutableListOf<String>()

                for (item in listResult.items) {
                    val path = item.path
                    imagePaths.add(path)
                }

                println("Lista ścieżek do zdjęć: $imagePaths")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Błąd pobierania listy elementów z Firebase Storage", exception)
            }

//        val apiClient = CarApiClient().create()
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
//        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment(carsListData))
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
        val categoryList = arrayListOf<String>()
        categoryList.add("ALL")
        categoryList.add("SPORT")
        categoryList.add("CITY")
        categoryList.add("PREMIUM")
        categoryList.add("OFF ROAD")

        carsList.forEach {car ->
            val cost = Random.nextInt(100, 1000)
            val category = categoryList[Random.nextInt(0, categoryList.size)]
            val randomImgPath = imagePaths[Random.nextInt(0, imagePaths.size)]

            val collectionRef = db.collection("carsData")
            val documentRef = collectionRef.document()

            val carData = hashMapOf(
                "id" to documentRef.id,
                "category" to category,
                "brand" to car.make.name,
                "model" to car.name,
                "url" to randomImgPath,
                "cost" to cost,
            )

            // Add a new document with a generated ID
            documentRef
                .set(carData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${car.id}")
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