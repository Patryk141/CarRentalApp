package com.example.carrental

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout

class CarAdapter(private var carsList : List<CarData?>, var favoriteCarsListData : List<CarData?>, private var context: Context, var listener : FavoriteInterface) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewModel : TextView = view.findViewById<TextView>(R.id.model)
        var textViewPrice : TextView = view.findViewById<TextView>(R.id.price)
        var image : AppCompatImageView = view.findViewById<AppCompatImageView>(R.id.appCompatImageView)
        var transformationLayout : TransformationLayout = view.findViewById<TransformationLayout>(R.id.transformationLayout)
        var button : Button = view.findViewById<Button>(R.id.button2)
    }
//    AIzaSyCBLE-n9vJsG3OMKqjykhGr7GokjGIcL84

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car: CarData? = carsList[position]
        var isFavorite : Boolean = false
        var imageUrl : String = ""
        val storageRef = FirebaseStorage.getInstance().reference.child(car!!.url)

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

        holder.textViewModel.text = car.brand + " " + car.model
        holder.textViewPrice.text = car.cost.toString() + "$/day"

        for (element in favoriteCarsListData) {
            if (car!!.id == element!!.id) {
                isFavorite = true
                holder.button.setBackgroundResource(R.drawable.baseline_favorite_24)
                break
            }
        }

        holder.image.setOnClickListener {
            val intent = Intent(context, com.example.carrental.Activities.CarDetailsActivity::class.java)
            intent.putExtra("ID", car?.id)
            intent.putExtra("url", imageUrl)
            TransformationCompat.startActivity(holder.transformationLayout, intent)
        }
        holder.button.setOnClickListener {
            if (isFavorite) {
                isFavorite = false
                holder.button.setBackgroundResource(R.drawable.baseline_favorite_border_24)
                listener.deleteCar(car)
            } else {
                isFavorite = true
                holder.button.setBackgroundResource(R.drawable.baseline_favorite_24)
                listener.addCar(car)
            }
        }
    }

}