package com.smartcar.voicesystem.reminderservice.handlers

import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Month

class AppointmentReminderHandlerTest {

    private val appointmentReminderHandler = AppointmentReminderHandler()

    @Test
    fun `process() should return domain reminder with APPOINTMENT type and due date`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder")

        val domainReminder = appointmentReminderHandler.process(reminderRequest)

        assertEquals(ReminderType.APPOINTMENT, domainReminder.type)
        assertEquals(2020, domainReminder.dueDate.year)
        assertEquals(Month.NOVEMBER, domainReminder.dueDate.month)
        assertEquals(10, domainReminder.dueDate.dayOfMonth)
    }
}
