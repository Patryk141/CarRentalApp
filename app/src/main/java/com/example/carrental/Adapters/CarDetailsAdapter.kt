package com.example.carrental.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.CarFeature
import com.example.carrental.R

class CarDetailsAdapter(var list : ArrayList<CarFeature>) : RecyclerView.Adapter<CarDetailsAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var featureTxtView : TextView = view.findViewById(R.id.featureTextView)
    var valueTxtView : TextView = view.findViewById(R.id.valueTxtView)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.car_detail_widget, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val carFeature: CarFeature = list[position]
    holder.featureTxtView.text = carFeature.feature
    holder.valueTxtView.text = carFeature.value.toString()
  }

  override fun getItemCount(): Int {
    return list.size
  }

}