package edu.wgu.faculty.office.hours.misc

import edu.wgu.faculty.office.hours.models.OfficeHourUiState

object OfficeHourComparator: Comparator<OfficeHourUiState> {
    private const val KEEP_THIS_ORDER = -1
    private const val REVERSE_ORDER = 1

    override fun compare(
        officeHour1: OfficeHourUiState,
        officeHour2: OfficeHourUiState,
    ): Int {
        // How do we show Sunday first?
        return officeHour1.dayOfWeek.compareTo(officeHour2.dayOfWeek)
    }
}