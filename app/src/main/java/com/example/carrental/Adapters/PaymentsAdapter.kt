package com.example.carrental.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.Data.Payment
import com.example.carrental.R

class PaymentsAdapter(var paymentsData: List<Payment>) : RecyclerView.Adapter<PaymentsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_payment, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: PaymentsAdapter.ViewHolder, position: Int) {
    val payment = paymentsData[position]
    println(payment.clientSecret)
    println(payment.currency)
    holder.carModel.text = payment.carModel
    holder.paymentID.text = payment.clientSecret
    holder.paymentPrice.text = payment.amount
  }

  override fun getItemCount(): Int {
    return paymentsData.size
  }


  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var carModel : TextView = view.findViewById<TextView>(R.id.carModel)
    var paymentID : TextView = view.findViewById<TextView>(R.id.paymentID)
    var paymentPrice : TextView = view.findViewById<TextView>(R.id.paymentPrice)
  }

}