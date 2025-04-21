package edu.wgu.faculty.office.hours.misc

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun LocalTime.toZonedDateTime(zonedId: ZoneId): ZonedDateTime {
    return ZonedDateTime.of(LocalDate.now(), this, zonedId)
}

fun String?.toLocalTime(inputFormat: String): LocalTime? {
    if (this.isNullOrBlank())
        return null

    val inputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(inputFormat)

    return try {
        LocalTime.parse(this, inputFormatter)
    } catch (exception: DateTimeParseException) {
        null
    }
}

fun ZonedDateTime.toString(format: String): String {
    try {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
        return format(formatter)
    } catch (e: DateTimeException) {
        throw e
    }
}