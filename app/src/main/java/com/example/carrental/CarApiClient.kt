package com.example.carrental

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarApiClient {

  private val baseURL = "https://carapi.app/api/"

  fun create() : CarApiService {
    val retrofit = Retrofit.Builder()
      .baseUrl(baseURL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    return retrofit.create(CarApiService::class.java)
  }

}