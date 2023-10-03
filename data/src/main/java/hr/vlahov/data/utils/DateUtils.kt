package hr.vlahov.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toISO8601Date(): String? =
    this.toDateFormat("yyyy-MM-ddTHH:mm:ss")

fun Long.toDateFormat(format: String, locale: Locale = Locale.US): String? =
    SimpleDateFormat(format, locale).format(Date(this))
