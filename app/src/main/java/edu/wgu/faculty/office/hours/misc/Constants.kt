package edu.wgu.faculty.office.hours.misc

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

const val ISO_TIME_AMPM = "hh:mm a"
val newYorkZoneId: ZoneId = ZoneId.of("America/New_York")

val standardOffset: ZoneOffset
    get() {
        val zone = ZoneId.systemDefault()
        val rules = zone.rules
        val instant = Instant.now()

        return rules.getStandardOffset(instant)
    }

fun standardOffsetFrom(zoneId: ZoneId): ZoneOffset {
    val rules = zoneId.rules
    val instant = Instant.now()

    return rules.getStandardOffset(instant)
}