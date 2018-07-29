package com.waltervjunior.smsreminder.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.asString(format : String = "dd/MM/yyyy") : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}