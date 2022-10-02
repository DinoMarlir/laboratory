package me.obsilabor.laboratory.utils

fun getRandomID(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('&', '/', '.', '$')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}