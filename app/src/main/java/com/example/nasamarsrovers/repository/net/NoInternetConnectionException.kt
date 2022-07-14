package com.example.nasamarsrovers.repository.net

import java.io.IOException

class NoInternetConnectionException: IOException() {
    override val message: String
        get() = "No Internet connection! Please check and retry."
}