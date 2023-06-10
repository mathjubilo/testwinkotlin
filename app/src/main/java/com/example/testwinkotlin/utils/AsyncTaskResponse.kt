package com.example.testwinkotlin.utils

sealed class AsyncTaskResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): AsyncTaskResponse<T>(data)
    class Error<T>(message: String, data: T? = null): AsyncTaskResponse<T>(data, message)
}