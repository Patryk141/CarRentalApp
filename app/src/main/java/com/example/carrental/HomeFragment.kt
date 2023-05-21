package com.example.carrental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private val categories: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.adapter = CategoryAdapter(generateCategory())
        val recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        recyclerViewCar.layoutManager = GridLayoutManager(activity,  2)
        recyclerViewCar.adapter = CarAdapter(generateCategory(), generateCategory(), activity as Context)
    }

    private fun generateCategory(): MutableList<String> {
        categories.clear()
        categories.add("ALL")
        categories.add("SPORT")
        categories.add("CITY")
        categories.add("PREMIUM")
        categories.add("OFF ROAD")
        return categories
    }
}