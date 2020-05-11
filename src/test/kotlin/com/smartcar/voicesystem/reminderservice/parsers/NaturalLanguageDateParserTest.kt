package com.smartcar.voicesystem.reminderservice.parsers

import com.smartcar.voicesystem.reminderservice.parsers.NaturalLanguageDateParser.parse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Month
import java.time.ZonedDateTime

class NaturalLanguageDateParserTest {

    @Test
    fun `should return correct date with current year when sentence has just ordinal and month`() {
        val sentence = "Hey Mercedes, I would like to go on SHOPPING on 1st July"
        val currentYear = ZonedDateTime.now().year

        val dueDate = parse(sentence)

        assertEquals(currentYear, dueDate.year)
        assertEquals(Month.JULY, dueDate.month)
        assertEquals(1, dueDate.dayOfMonth)
    }

    @Test
    fun `should return correct date with time when sentence has both date and time`() {
        val sentence = "Hey Mercedes, I would like to go on SHOPPING on 1st July at 10.30 am"

        val dueDate = parse(sentence)

        assertEquals(Month.JULY, dueDate.month)
        assertEquals(1, dueDate.dayOfMonth)
        assertEquals(30, dueDate.minute)
    }

    @Test
    fun `should return current date when not able to parse the sentence`() {
        val sentence = "Hey Mercedes, add a reminder for paying house rent"
        val currentDate = ZonedDateTime.now()

        val dueDate = parse(sentence)

        assertEquals(currentDate.month, dueDate.month)
        assertEquals(currentDate.dayOfMonth, dueDate.dayOfMonth)
        assertEquals(currentDate.year, dueDate.year)
    }
}
