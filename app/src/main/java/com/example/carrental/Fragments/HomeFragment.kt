package com.example.carrental.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.Adapters.CarAdapter
import com.example.carrental.CarData
import com.example.carrental.Adapters.CategoryAdapter
import com.example.carrental.R

class HomeFragment(var carsListData : List<CarData?>) : Fragment() {

    private val categories: MutableList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.adapter = CategoryAdapter(generateCategory())
        val recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerViewAdapter = CarAdapter(generateCategory(), generateCategory(), carsListData, activity as Context)
        recyclerViewCar.layoutManager = GridLayoutManager(activity, 2)
        recyclerViewCar.adapter = recyclerViewAdapter
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