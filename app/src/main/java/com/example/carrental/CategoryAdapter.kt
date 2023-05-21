package com.example.carrental

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: List<String>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val viewHolder : MutableList<ViewHolder> = ArrayList()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView : TextView = view.findViewById<TextView>(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_category, parent, false)
        viewHolder.add(ViewHolder(view))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = categories[position]
        holder.textView.setOnClickListener {
            for (i in categories.indices){
                viewHolder[i].textView.setTextColor(Color.BLACK)
            }
            holder.textView.setTextColor(Color.RED)
        }
    }
}