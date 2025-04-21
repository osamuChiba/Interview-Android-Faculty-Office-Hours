package edu.wgu.faculty.office.hours

import org.junit.Test
import org.junit.Assert.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.wgu.faculty.office.hours.models.OfficeHourUiState
import edu.wgu.faculty.office.hours.models.ShiftResponse
import edu.wgu.faculty.office.hours.models.toUiState

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