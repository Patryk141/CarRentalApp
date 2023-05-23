package com.example.carrental

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