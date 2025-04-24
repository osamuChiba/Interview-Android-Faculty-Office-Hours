package edu.wgu.faculty.office.hours.models

import edu.wgu.faculty.office.hours.misc.ISO_TIME_AMPM
import edu.wgu.faculty.office.hours.misc.newYorkZoneId
import edu.wgu.faculty.office.hours.misc.standardOffset
import edu.wgu.faculty.office.hours.misc.standardOffsetOf
import edu.wgu.faculty.office.hours.misc.toLocalTime
import edu.wgu.faculty.office.hours.misc.toString
import edu.wgu.faculty.office.hours.misc.toZonedDateTime
import java.io.Serializable
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.UUID

data class OfficeHourEntity(
    val startDay: String?, // MON, TUE, WED, etc.
    val endDay: String?, // MON, TUE, WED, etc.
    val startTime: String?, // "HHmm"
    val endTime: String?, // "HHmm"
    val offset: String?, // -7, -5, etc.
    val id: String = UUID.randomUUID().toString()
) : Serializable {
    // Default = Mountain Time.  Note: offset is STANDARD, not daylight savings.
    private val offsetHours: Int
        get() {
            return offset?.toInt() ?: -7
        }

    private val localizedStartTime: String?
        get() {
            if (startTime.isNullOrBlank())
                return null

            return localizeTimeWithOffset(
                inputTime = startTime,
                inputFormat = "HHmm",
                outputFormat = ISO_TIME_AMPM,
                offsetHours = offsetHours,
                // ← Answer for the Question 3
                // normalized() does not do anything for those countries with no daylight savings.
                outputZoneId = standardOffsetOf(newYorkZoneId).normalized()
            )
        }

    private val localizedEndTime: String?
        get() {
            if (endTime.isNullOrBlank())
                return null

            return localizeTimeWithOffset(
                inputTime = endTime,
                inputFormat = "HHmm",
                outputFormat = ISO_TIME_AMPM,
                offsetHours = offsetHours,
                // ← Answer for the Question 3
                outputZoneId = standardOffsetOf(newYorkZoneId).normalized()
            )
        }

    // Returns something like: 6:30 AM - 2:30 PM
    val officeHour: String
        get() {
            if (localizedStartTime.isNullOrBlank() || localizedEndTime.isNullOrBlank())
                return ""

            return "$localizedStartTime - $localizedEndTime".uppercase()
        }
}

fun localizeTimeWithOffset(
    inputTime: String?,
    inputFormat: String,
    outputFormat: String,
    offsetHours: Int,
    outputZoneId: ZoneId = standardOffset.normalized(),
): String? {
    val inputZoneOffset = ZoneOffset.ofHours(offsetHours)
    val inputZoneId = inputZoneOffset.normalized()

    return inputTime
        .toLocalTime(inputFormat)
        ?.toZonedDateTime(inputZoneId)
        ?.withZoneSameInstant(outputZoneId)
        ?.toString(outputFormat)
}