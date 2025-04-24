package edu.wgu.faculty.office.hours.misc

import edu.wgu.faculty.office.hours.models.OfficeHourUiState
import java.time.DayOfWeek

object OfficeHourComparator: Comparator<OfficeHourUiState> {
    private const val KEEP_THIS_ORDER = -1
    private const val REVERSE_ORDER = 1

    override fun compare(
        officeHour1: OfficeHourUiState,
        officeHour2: OfficeHourUiState,
    ): Int {
        // How do we show Sunday first?

        // ↓ Answer for the Question 2 (both of these if statements)
        if (officeHour1.dayOfWeek == DayOfWeek.SUNDAY && officeHour2.dayOfWeek != DayOfWeek.SUNDAY)
            return KEEP_THIS_ORDER

        if (officeHour1.dayOfWeek != DayOfWeek.SUNDAY && officeHour2.dayOfWeek == DayOfWeek.SUNDAY)
            return REVERSE_ORDER

        return officeHour1.dayOfWeek.compareTo(officeHour2.dayOfWeek)
    }
}