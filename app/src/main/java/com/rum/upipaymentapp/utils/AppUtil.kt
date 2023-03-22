package com.rum.upipaymentapp.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes messageRes: Int) {
    toast(resources.getString(messageRes))
}

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}