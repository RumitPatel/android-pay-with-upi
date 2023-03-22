package com.rum.upipaymentapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.isConnectionAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val netInfo = connectivityManager.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected && netInfo.isConnectedOrConnecting && netInfo.isAvailable) {
            return true
        }
    }
    return false
}

fun Context.toast(@StringRes messageRes: Int) {
    toast(resources.getString(messageRes))
}

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}