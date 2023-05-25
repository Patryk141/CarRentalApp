package com.example.carrental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.transformationlayout.onTransformationStartContainer

class FavoriteFragment(var carsListData : List<CarData?>, var favoriteCarsListData : List<CarData?>, var listener : FavoriteInterface) : Fragment() {
    private lateinit var recyclerViewCar: RecyclerView
    private lateinit var layoutAnimationController: LayoutAnimationController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerViewCar = view.findViewById<RecyclerView>(R.id.recyclerView3)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewAdapter = CarAdapter(carsListData, favoriteCarsListData, activity as Context, listener)
        recyclerViewCar.layoutManager = GridLayoutManager(activity, 2)
        recyclerViewCar.adapter = recyclerViewAdapter
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.recycler_grid_layout_animation)
        recyclerViewCar.layoutAnimation = layoutAnimationController
    }
}
    // db logic to view all cars added to the favorites
