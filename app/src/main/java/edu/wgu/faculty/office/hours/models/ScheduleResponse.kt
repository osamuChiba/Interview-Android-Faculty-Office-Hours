package edu.wgu.faculty.office.hours.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    @Json(name = "Day")
    val day: String?,

    @Json(name = "Time")
    val time: String?,
)
