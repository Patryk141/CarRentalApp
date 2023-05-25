package com.example.carrental.Adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carrental.CarData
import com.example.carrental.Activities.CarDetailsActivity
import com.example.carrental.R
import com.google.firebase.storage.FirebaseStorage

class CarAdapter(private var name: List<String>, private var price: List<String>, private var carsList : List<CarData?>, private var context: Context) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewModel : TextView = view.findViewById<TextView>(R.id.model)
        var textViewPrice : TextView = view.findViewById<TextView>(R.id.price)
        var image : AppCompatImageView = view.findViewById<AppCompatImageView>(R.id.appCompatImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
//        return urls.size
        return carsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val car: CarData? = carsList[position]
        var imageUrl : String = ""
        val storageRef = FirebaseStorage.getInstance().getReference().child(car!!.url)

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUrl = uri.toString()
            println(imageUrl)

            Glide.with(holder.itemView)
                .load(imageUrl)
//            .override(500, 500)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image)
        }
        .addOnFailureListener { exception ->
            // Obsługa błędu
            Log.e(TAG, "Error getting download URL: $exception")
        }
//        "gs://carrental-mobile.appspot.com" + car!!.url

        holder.textViewModel.text = car.brand + " " + car.model
        holder.textViewPrice.text = car.cost.toString() + "$/day"

        holder.image.setOnClickListener {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra("ID", car?.id)
            intent.putExtra("imagePath", imageUrl)
            val pair = android.util.Pair<View, String>(holder.image, "image")
            val activityOptions = ActivityOptions.makeSceneTransitionAnimation(context as Activity?, pair)
            context.startActivity(intent, activityOptions.toBundle())
        }
    }

}