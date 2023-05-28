package com.example.carrental.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.Adapters.CarAdapter
import com.example.carrental.Adapters.CategoryAdapter
import com.example.carrental.CategoryInterface
import com.example.carrental.Data.CarData
import com.example.carrental.FavoriteInterface
import com.example.carrental.R

class HomeFragment(var carsListData: List<CarData?>, var favoriteCarsListData: List<CarData?>, var listener : FavoriteInterface, var category: String, var searchingText: String) : Fragment(),
    CategoryInterface {

    private val categories: MutableList<String> = ArrayList()
    private lateinit var search : EditText
    private lateinit var recyclerViewCar : RecyclerView
    private lateinit var layoutAnimationController : LayoutAnimationController
    private lateinit var buttonClear : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        search = view.findViewById<EditText>(R.id.editTextTextPersonName)
        recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        buttonClear = view.findViewById<Button>(R.id.buttonClose)
        view?.findViewById<Button>(R.id.button)?.setOnClickListener { buttonSearch(it) }
        buttonClear.setOnClickListener { clearEditText(it) }

        search.setText(searchingText)
        if (searchingText.isNotEmpty()) {
            buttonClear.isVisible = true
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        listener.change(category, searchingText)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.recycler_grid_layout_animation)
        recyclerViewCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.adapter = CategoryAdapter(generateCategory(), this, category)
        recyclerViewCar.layoutManager = GridLayoutManager(activity, 2)
        categoryClick(category)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.isVisible = s!!.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
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

    override fun categoryClick(string: String) {
        val carsWithFilters = ArrayList<CarData?>()
        category = if (string == "ALL") {
            ""
        } else {
            string
        }
        searchingText = search.text.toString()
        val string = searchingText.split(" ")
        var counter : Int
        for (i in carsListData.indices) {
            counter = 0
            for (j in string.indices) {
                if ((carsListData[i]!!.brand.lowercase().contains(string[j].lowercase()) || carsListData[i]!!.model.lowercase().contains(string[j].lowercase())) && carsListData[i]!!.category.contains(category)) {
                    counter++
                }
            }
            if (counter == string.size) {
                carsWithFilters.add(carsListData[i])
            }
        }
        val recyclerViewAdapter = CarAdapter(carsWithFilters, favoriteCarsListData, activity as Context, listener)
        recyclerViewCar?.adapter = recyclerViewAdapter
        recyclerViewCar.layoutAnimation = layoutAnimationController
    }

    private fun clearEditText(view: View) {
        searchingText = ""
        search.text.clear()
        buttonClear.isVisible = false
        val carsWithFilters = ArrayList<CarData?>()
        for (i in carsListData.indices) {
            if (carsListData[i]!!.category.contains(category)) {
                carsWithFilters.add(carsListData[i])
            }
        }
        val recyclerViewAdapter = CarAdapter(carsWithFilters, favoriteCarsListData, activity as Context, listener)
        recyclerViewCar?.adapter = recyclerViewAdapter
        recyclerViewCar.layoutAnimation = layoutAnimationController
    }

    private fun buttonSearch(view: View) {
        val carsWithFilters = ArrayList<CarData?>()
        searchingText = search.text.toString()
        val string = searchingText.split(" ")
        var counter : Int
        for (i in carsListData.indices) {
            counter = 0
            for (j in string.indices) {
                if ((carsListData[i]!!.brand.lowercase().contains(string[j].lowercase()) || carsListData[i]!!.model.lowercase().contains(string[j].lowercase())) && carsListData[i]!!.category.contains(category)) {
                    counter++
                }
            }
            if (counter == string.size) {
                carsWithFilters.add(carsListData[i])
            }
        }
        val recyclerViewAdapter = CarAdapter(carsWithFilters, favoriteCarsListData, activity as Context, listener)
        recyclerViewCar?.adapter = recyclerViewAdapter
        recyclerViewCar.layoutAnimation = layoutAnimationController
    }
}