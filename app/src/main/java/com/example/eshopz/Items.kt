package com.example.eshopz

data class Items(
    val Category: String? = "",
    val Condition: String? = "",
    val Description: String? = "",
    val Free_shipping: Boolean? = true,
    val ImageURL: String? = "",
    val Meetup: Boolean? = true,
    val Name: String? = "",
    val Price: Double? = 0.00,
    val Rating: Double? = 0.00,
    val Sales: Int? = 0,
    val Seller: String? = "",
    val Stock: Int? = 0,
    val Location: String? = "",
    val UID:String? = ""
)

