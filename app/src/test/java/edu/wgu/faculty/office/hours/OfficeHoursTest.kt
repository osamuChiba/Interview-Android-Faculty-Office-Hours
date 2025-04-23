package edu.wgu.faculty.office.hours

import org.junit.Test
import org.junit.Assert.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.wgu.faculty.office.hours.misc.newYorkZoneId
import edu.wgu.faculty.office.hours.misc.toString
import edu.wgu.faculty.office.hours.misc.toZonedDateTime
import edu.wgu.faculty.office.hours.models.OfficeHourUiState
import edu.wgu.faculty.office.hours.models.ScheduleResponse
import edu.wgu.faculty.office.hours.models.ShiftResponse
import edu.wgu.faculty.office.hours.models.toUiState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class OfficeHoursTest {
    private val moshi = Moshi.Builder().build()
    private val listType =
        Types.newParameterizedType(List::class.java, ShiftResponse::class.java)
    private val adapter: JsonAdapter<List<ShiftResponse>> = moshi.adapter(listType)

    // Don't worry about !!
    private val officeHoursResponse = adapter.fromJson(officeHours)!!

    private fun toUiStates(): List<OfficeHourUiState> {
        return officeHoursResponse.mapNotNull { shift ->
            shift.toOfficeHourEntity().toUiState()
        }
    }

    @Test
    fun incorrectOrder() {
        // Just so we can run this as a test...
        assertEquals(officeHoursResponse.size, 7)

        val officeHours = toUiStates()

        officeHours.forEach { officeHour ->
            println("${officeHour.dayOfWeekShort} ${officeHour.officeHour}")
        }
    }

    @Test
    fun correctOrder() {
        assertTrue(true)

        val officeHours = toUiStates()

        val sortedOfficeHours = mutableListOf<OfficeHourUiState>()

        sortedOfficeHours.addAll(officeHours.sortedWith(compareBy { it.dayOfWeek.value }))
        val sundays = sortedOfficeHours.filter { it.dayOfWeek.value == 7 }
        sortedOfficeHours.removeAll(sundays)
        sundays.forEach { sunday -> sortedOfficeHours.addFirst(sunday) }

        sortedOfficeHours.toList().forEach { officeHour ->
            println("${officeHour.dayOfWeekShort} ${officeHour.officeHour}")
        }
    }

    @Test
    fun correctOrderAndNewYorkTime() {
        assertTrue(true)

        val mutableHoursResponse = mutableListOf<ShiftResponse>()
        val sortedOfficeHours = mutableListOf<OfficeHourUiState>()

        officeHoursResponse.forEach { rawOfficeHour ->
            val shiftResponse = ShiftResponse(
                start = ScheduleResponse(
                    day = rawOfficeHour.start?.day, time = convertToTimezone(
                        hour = rawOfficeHour.start?.time ?: "",
                        offset = rawOfficeHour.offset?.toInt() ?: 0
                    )
                ),
                end = ScheduleResponse(
                    day = rawOfficeHour.end?.day, time = convertToTimezone(
                        hour = rawOfficeHour.end?.time ?: "",
                        offset = rawOfficeHour.offset?.toInt() ?: 0
                    )
                ),
                offset = rawOfficeHour.offset
            )
            mutableHoursResponse.add(shiftResponse)
        }
        sortedOfficeHours.addAll(mutableHoursResponse.mapNotNull { shift ->
            shift.toOfficeHourEntity().toUiState()
        }.sortedWith(compareBy { it.dayOfWeek.value }))

        val sundays = sortedOfficeHours.filter { it.dayOfWeek.value == 7 }
        sortedOfficeHours.removeAll(sundays)
        sundays.forEach { sunday -> sortedOfficeHours.addFirst(sunday) }

        sortedOfficeHours.toList().forEach { officeHour ->
            println("${officeHour.dayOfWeekShort} ${officeHour.officeHour}")
        }
    }

    /**
     * Converts UTC hour string literal to
     */
    fun convertToTimezone(
        hour: String,
        timezone: ZoneId = newYorkZoneId,
        offset: Int,
        pattern: String = "HHmm"
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localTime = LocalTime.parse(hour, formatter)
        val localDateTime = LocalDateTime.of(LocalDate.now(), localTime)
        val timeZoneOffset =
            localDateTime.atOffset(ZoneOffset.UTC)
        timeZoneOffset.withOffsetSameInstant(ZoneOffset.ofHours(offset))
        val convertedDateTime =
            localDateTime.atOffset(timeZoneOffset.offset).atZoneSameInstant(timezone)
        return convertedDateTime.toString(pattern)
    }

    // 1) Fix the incorrect order shown above.
    // 2) Show Sunday first, so the order will be Sunday, Monday, ... Saturday.
    // 3) Show the office hours in New York time (use newYorkZoneId).  Ok to hardcode somewhere.
}

val officeHours = """
[
    {
        "Start" : {
            "Day" : "MON",
            "Time" : "0700"
        },
        "End" : {
            "Day" : "MON",
            "Time" : "1500"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "TUE",
            "Time" : "0630"
        },
        "End" : {
            "Day" : "TUE",
            "Time" : "1400"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "WED",
            "Time" : "0700"
        },
        "End" : {
            "Day" : "WED",
            "Time" : "1500"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "THU",
            "Time" : "0900"
        },
        "End" : {
            "Day" : "THU",
            "Time" : "1500"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "MON",
            "Time" : "1700"
        },
        "End" : {
            "Day" : "MON",
            "Time" : "1900"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "SUN",
            "Time" : "1100"
        },
        "End" : {
            "Day" : "SUN",
            "Time" : "1730"
        },
        "Offset" : "-7"
    },
    {
        "Start" : {
            "Day" : "WED",
            "Time" : "1700"
        },
        "End" : {
            "Day" : "WED",
            "Time" : "1900"
        },
        "Offset" : "-7"
    }
]
""".trimIndent()