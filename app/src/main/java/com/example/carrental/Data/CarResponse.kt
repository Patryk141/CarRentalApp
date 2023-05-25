package com.example.carrental

import java.lang.reflect.Constructor

data class CarResponse(
  val collection: CollectionInfo,
  val data: List<Car>
)

data class CollectionInfo(
  val url: String,
  val count: Int,
  val pages: Int,
  val total: Int,
  val next: String,
  val prev: String,
  val first: String,
  val last: String
)

data class Car(
  val id: Int,
  val make_id: Int,
  val name: String,
  val make: Make
)

data class Make(
  val id: Int,
  val name: String
)

data class CarData(
  var id: String = "",
  var brand: String = "",
  var category: String = "",
  var cost: Int = 0,
  var model : String = "",
  var url : String = ""
) {
  // Konstruktor bezargumentowy
  constructor() : this("", "", "", 0, "", "")
}