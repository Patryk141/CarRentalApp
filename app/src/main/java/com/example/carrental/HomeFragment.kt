package com.example.carrental

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment(private val imageUrls : ArrayList<String>) : Fragment(), CategoryInterface {
    private val categories: ArrayList<String> = ArrayList()
    private lateinit var search : EditText
    private lateinit var recyclerViewCar : RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        search = view.findViewById<EditText>(R.id.editTextTextPersonName)
        recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        view?.findViewById<Button>(R.id.button)?.setOnClickListener { buttonSearch(it) }
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.adapter = CategoryAdapter(generateCategory(), this)
        val recyclerViewAdapter = CarAdapter(generateCategory(), generateCategory(), imageUrls, activity as Context)
        recyclerViewCar.layoutManager = GridLayoutManager(activity, 2)
        recyclerViewCar.adapter = recyclerViewAdapter
    }

    private fun generateCategory(): MutableList<String> {
        categories.clear()
        categories.add("BMW")
        categories.add("AUDI")
        categories.add("PORSCHE")
        categories.add("MERCEDES")
        categories.add("JEEP")
        return categories
    }

    override fun categoryClick(string: String) {
        val imageWithFilters = ArrayList<String>()
        for (i in 0 until imageUrls.size) {
            if (imageUrls[i].contains(string.lowercase()))
                imageWithFilters.add(imageUrls[i])
        }
        val recyclerViewCar = view?.findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerViewAdapter = CarAdapter(generateCategory(), generateCategory(), imageWithFilters, activity as Context)
        recyclerViewCar?.adapter = recyclerViewAdapter
    }

    private fun buttonSearch(view: View) {
        val imageWithFilters = ArrayList<String>()
        Log.e("", search.text.toString())
        if (search != null) {
            for (i in 0 until imageUrls.size) {
                if (imageUrls[i].contains(search.text))
                    imageWithFilters.add(imageUrls[i])
            }
            val recyclerViewAdapter = CarAdapter(generateCategory(), generateCategory(), imageWithFilters, activity as Context)
            recyclerViewCar.adapter = recyclerViewAdapter
        }
    }
}