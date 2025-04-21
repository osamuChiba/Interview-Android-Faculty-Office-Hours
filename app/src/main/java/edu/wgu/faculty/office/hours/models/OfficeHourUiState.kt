package edu.wgu.faculty.office.hours.models

import java.time.DayOfWeek

data class OfficeHourUiState(
    val dayOfWeek: DayOfWeek,
    val officeHour: String,
) {
    val dayOfWeekShort: String = dayOfWeek.name.uppercase().substring(0, 3)
}

fun OfficeHourEntity.toUiState(): OfficeHourUiState? {
    if (this.startDay.isNullOrBlank())
        return null

    try {
        val found = DayOfWeek.entries.first { dayOfWeek ->
            dayOfWeek.name.startsWith(this.startDay, true)
        }

        return OfficeHourUiState(
            dayOfWeek = found,
            officeHour)
    } catch (error: Exception) {
    }
    return null
}
