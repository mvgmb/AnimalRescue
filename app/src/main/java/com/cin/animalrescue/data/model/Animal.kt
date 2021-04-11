package com.cin.animalrescue.data.model

data class Animal(
    var id: String,
    var helper: String,
    var type: String,
    var title: String,
    val location: String,
    var info: String
) {
    override fun toString(): String {
        return title
    }
}
