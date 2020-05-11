package com.smartcar.voicesystem.reminderservice.parsers

import com.joestelmach.natty.Parser
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

object NaturalLanguageDateParser {

    private val dateParser = Parser()

    fun parse(sentence: String): ZonedDateTime {
        val dateGroups = dateParser.parse(sentence)
        if (!dateGroups.isEmpty()) {
            val firstGroup = dateGroups.first()
            val dates = firstGroup.dates

            if (!dates.isEmpty()) {
                return ZonedDateTime.ofInstant(dates[0].toInstant(), UTC)
            }
        }

        return ZonedDateTime.now(UTC)
    }
}
