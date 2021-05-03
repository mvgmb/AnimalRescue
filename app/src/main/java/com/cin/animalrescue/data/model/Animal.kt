package com.cin.animalrescue.data.model

data class Animal(
    var id: String,
    var helper_uid: String,
    var helper_name: String,
    var type: String,
    var title: String,
    var location: String,
    var latitude: Double,
    var longitude: Double,
    var info: String,
    var image_uri: String,
) {
    override fun toString(): String {
        return title
    }
}
