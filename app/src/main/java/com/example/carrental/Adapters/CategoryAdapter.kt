package com.example.carrental.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.CategoryInterface
import com.example.carrental.R

class CategoryAdapter(private var categories: List<String>, private val listener: CategoryInterface, private val clicked: String) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
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
        if (categories[position] == clicked) {
            holder.textView.setTextColor(Color.WHITE)
            holder.textView.setBackgroundResource(R.drawable.category_background2)
        }
        holder.textView.setOnClickListener {
            listener.categoryClick(categories[position])
            for (i in viewHolder.indices){
                viewHolder[i].textView.setTextColor(Color.BLACK)
                viewHolder[i].textView.setBackgroundResource(R.drawable.category_background1)
            }
            holder.textView.setTextColor(Color.WHITE)
            holder.textView.setBackgroundResource(R.drawable.category_background2)
        }
    }
}