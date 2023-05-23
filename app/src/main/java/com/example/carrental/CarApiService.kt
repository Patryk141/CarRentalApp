package com.example.carrental

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CarApiService {
  @GET("/api/models")
  fun getCarsData(
    @Query("page") page: Int,
    @Query("verbose") verbose: String,
    @Query("year") year: Int
  ) : Call<CarResponse>
}