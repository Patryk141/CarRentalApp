package com.example.carrental

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.transformationlayout.onTransformationStartContainer
import kotlinx.coroutines.CoroutineScope

class HomeFragment(var carsListData: List<CarData?>, var favoriteCarsListData: List<CarData?>, var listener : FavoriteInterface) : Fragment(), CategoryInterface {

    private val categories: MutableList<String> = ArrayList()
    private lateinit var search : EditText
    private lateinit var recyclerViewCar : RecyclerView
    private lateinit var layoutAnimationController : LayoutAnimationController
    private var category = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        search = view.findViewById<EditText>(R.id.editTextTextPersonName)
        recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        view?.findViewById<Button>(R.id.button)?.setOnClickListener { buttonSearch(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.adapter = CategoryAdapter(generateCategory(), this)
        val recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerViewAdapter = CarAdapter(carsListData, favoriteCarsListData, activity as Context, listener)
        recyclerViewCar.layoutManager = GridLayoutManager(activity, 2)
        recyclerViewCar.adapter = recyclerViewAdapter
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.recycler_grid_layout_animation)
        recyclerViewCar.layoutAnimation = layoutAnimationController
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
        val string = search.text.split(" ")
        var counter = 0
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