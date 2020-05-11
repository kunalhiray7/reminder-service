package com.smartcar.voicesystem.reminderservice.handlers

import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Month

class GeneralReminderHandlerTest {

    private val generalReminderHandler = GeneralReminderHandler()

    @Test
    fun `process() should return domain reminder with GENERAL type and due date`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, add a reminder to pay the house rent on 1st July")

        val domainReminder = generalReminderHandler.process(reminderRequest)

        assertEquals(ReminderType.GENERAL, domainReminder.type)
        assertEquals(2020, domainReminder.dueDate.year)
        assertEquals(Month.JULY, domainReminder.dueDate.month)
        assertEquals(1, domainReminder.dueDate.dayOfMonth)
    }
}
