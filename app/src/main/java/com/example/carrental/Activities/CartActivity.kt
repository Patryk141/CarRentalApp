package com.example.carrental.Activities

import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.example.carrental.R
import com.example.carrental.FirebaseAuthSingleton
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.PaymentConfiguration
import okhttp3.*
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class CartActivity : AppCompatActivity() {

  private lateinit var imageCart : ImageView
  private lateinit var carModel : TextView
  private lateinit var carPrice : TextView
  private lateinit var paymentBtn : Button
  private lateinit var db : FirebaseFirestore
  private lateinit var auth: FirebaseAuth
  lateinit var customerConfig: PaymentSheet.CustomerConfiguration
  private lateinit var paymentIntentClientSecret: String
  private lateinit var paymentSheet: PaymentSheet
  private lateinit var paymentSheetLauncher: ActivityResultLauncher<Intent>

  companion object {
//    private const val BACKEND_URL = "http://10.0.2.2:4245/payment-sheet"
    private const val BACKEND_URL = "http://192.168.0.66:4245/payment-sheet"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cart)

    db = Firebase.firestore
    auth = FirebaseAuthSingleton.getInstance()

    paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

    val imagePath = intent.getStringExtra("imagePath")
    val cost = intent.getStringExtra("cost")
    val model = intent.getStringExtra("model")

    imageCart = findViewById(R.id.imageCart)
    carModel = findViewById(R.id.carModel)
    paymentBtn = findViewById(R.id.paymentBtn)
    carPrice = findViewById(R.id.carPrice)

    paymentBtn.setOnClickListener { // payment initialization
      presentPaymentSheet()
    }
    initActivity(model, cost!!.toInt(), imagePath)

    var jsonBody = JSONObject()
    jsonBody.put("price", carPrice.text)
    var requestBody = jsonBody.toString()

    BACKEND_URL.httpPost()
      .header("Content-Type", "application/json")
      .body(requestBody)
      .responseJson { _, _, result ->
        println("No siema siemanko witam")
        if (result is Result.Success) {
          val responseJson = result.get().obj()
          customerConfig = PaymentSheet.CustomerConfiguration(
            responseJson.getString("customer"),
            responseJson.getString("ephemeralKey")
          )
          paymentIntentClientSecret = responseJson.getString("paymentIntent")
          val publishableKey = responseJson.getString("publishableKey")
          PaymentConfiguration.init(this, publishableKey)
        }
        else {
          println(result)
        }
      }

//    fetchPaymentIntent()
  }

  private fun initActivity(model: String?, cost: Int, imagePath: String?) {
    carModel.text = model
    carPrice.text = cost.toString() + "$"
    Glide.with(this)
      .load(imagePath)
      .into(imageCart)
  }

  fun presentPaymentSheet() {
    var googlePayConfiguration = PaymentSheet.GooglePayConfiguration(
      environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
      countryCode = "US",
      currencyCode = "USD" // Required for Setup Intents, optional for Payment Intents
    )
    var configuration =  PaymentSheet.Configuration(
      merchantDisplayName = "Car Payment",
      customer = customerConfig,
      // Set `allowsDelayedPaymentMethods` to true if your business
      // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
//      allowsDelayedPaymentMethods = false
    )

//    configuration.googlePay = googlePayConfiguration
    if(paymentIntentClientSecret != null) {
      paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        configuration
      )
    }
  }

  fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when(paymentSheetResult) {
      is PaymentSheetResult.Canceled -> {
        Toast.makeText(this, "Your payment was cancelled", Toast.LENGTH_SHORT).show()
      }
      is PaymentSheetResult.Failed -> {
        Toast.makeText(this, "Your payment was failed", Toast.LENGTH_SHORT).show()
        print("Error: ${paymentSheetResult.error}")
      }
      is PaymentSheetResult.Completed -> {
        Toast.makeText(this, "Your payment was successful", Toast.LENGTH_SHORT).show()
        // Zapis do bazy
        GlobalScope.launch(Dispatchers.IO) {
          db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("payments")
            .add(hashMapOf(
              "amount" to carPrice.text,
              "carModel" to carModel.text,
              "currency" to "USD",
              "clientSecret" to paymentIntentClientSecret,
            ))
            .addOnSuccessListener { documentReference ->
              println("Document added with ID: ${documentReference.id}")/**/
              val intent = Intent(this@CartActivity, MainActivity::class.java)
              startActivity(intent)
            }
            .addOnFailureListener { e ->
              println("Error adding document: $e")
            }
        }
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
      }
    }
  }


}