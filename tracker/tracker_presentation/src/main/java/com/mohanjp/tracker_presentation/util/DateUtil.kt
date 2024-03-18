package com.mohanjp.tracker_presentation.util

import com.mohanjp.core.util.UiText
import com.mohanjp.core.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {

    fun parseDateToReadable(date: LocalDate): UiText {
        val today = LocalDate.now()

        return when(date) {
            today -> UiText.StringResource(R.string.today)
            today.minusDays(1) -> UiText.StringResource(R.string.yesterday)
            today.plusDays(1) -> UiText.StringResource(R.string.tomorrow)
            else -> UiText.DynamicString(
                DateTimeFormatter.ofPattern("dd LLLL").format(date)
            )
        }
    }
}