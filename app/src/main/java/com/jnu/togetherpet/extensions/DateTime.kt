package com.jnu.togetherpet.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formattingLocalDateTimeToString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return this.format(formatter)
}

fun formatDateTime(input: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E) HH:mm", Locale("ko", "KR"))
    return LocalDateTime.parse(input, inputFormatter).format(outputFormatter)
}
