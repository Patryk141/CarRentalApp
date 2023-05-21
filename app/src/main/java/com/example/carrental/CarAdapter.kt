package com.example.carrental

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(private var name: List<String>, private var price: List<String>, private var context: Context) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
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
        return name.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(R.drawable.audi)
        holder.image.setOnClickListener {
            val intent = Intent(context, CarDetailsActivity::class.java)

            val pair = android.util.Pair<View, String>(holder.image, "image")
            val activityOptions = ActivityOptions.makeSceneTransitionAnimation(context as Activity?, pair)

            context.startActivity(intent, activityOptions.toBundle())
        }
    }
}