package com.example.carrental

import com.example.carrental.Data.CarData

interface FavoriteInterface {
    fun addCar(car: CarData)
    fun deleteCar(car: CarData)
    fun change(category: String, searchingText: String)
}