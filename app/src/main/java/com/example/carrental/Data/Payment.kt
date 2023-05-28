package com.example.carrental.Data

import java.util.Currency

data class Payment(
  var amount: String = "",
  var carModel: String = "",
  var clientSecret: String = "",
  var currency: String = "",
)
