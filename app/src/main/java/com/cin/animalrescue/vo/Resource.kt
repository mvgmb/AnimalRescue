package com.cin.animalrescue.vo

data class Resource<out T>(val status: Status, val message: String?, val data: T?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, null, data)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, msg, data)
        }
    }

    fun isSuccess() : Boolean {
        return status == Status.SUCCESS
    }
}
