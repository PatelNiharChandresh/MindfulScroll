package com.rudy.mindfulscroll.util.extensions

/**
 * Formats a duration in seconds to MM:SS display format.
 */
fun Int.toFormattedTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}
