package com.example.ecoshine

import java.io.Serializable

data class ProductModel(
    val house_number: String = ""
) : Serializable {
    override fun toString(): String {
        return "ProductModel(house_number='$house_number')"
    }
}
