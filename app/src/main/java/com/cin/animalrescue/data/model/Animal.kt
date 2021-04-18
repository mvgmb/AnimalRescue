package com.cin.animalrescue.data.model

data class Animal(
    var id: String,
    var helper_uid: String,
    var type: String,
    var title: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    var info: String
) {
    override fun toString(): String {
        return title
    }
}
