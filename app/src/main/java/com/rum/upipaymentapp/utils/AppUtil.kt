package com.rum.upipaymentapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes

@SuppressLint("ObsoleteSdkInt")
fun Context.isConnectionAvailable(): Boolean {
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return this.getNetworkCapabilities(this.activeNetwork)?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) ?: false
        } else {
            (@Suppress("DEPRECATION")
            return this.activeNetworkInfo?.isConnected ?: false)
        }
    }
}

fun Context.toast(@StringRes messageRes: Int) {
    toast(resources.getString(messageRes))
}

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}