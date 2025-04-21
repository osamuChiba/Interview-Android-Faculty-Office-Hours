package edu.wgu.faculty.office.hours.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    @Json(name = "Start")
    val start: ScheduleResponse?,

    @Json(name = "End")
    val end: ScheduleResponse?,

    @Json(name = "Offset")
    val offset: String?,
) {
    fun toOfficeHourEntity(): OfficeHourEntity {
        return OfficeHourEntity(startDay = start?.day,
            endDay = end?.day,
            startTime = start?.time,
            endTime = end?.time,
            offset = offset)
    }
}
