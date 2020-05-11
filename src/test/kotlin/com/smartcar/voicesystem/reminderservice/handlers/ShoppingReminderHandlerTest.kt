package com.smartcar.voicesystem.reminderservice.handlers

import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Month

class ShoppingReminderHandlerTest {

    private val shoppingReminderHandler = ShoppingReminderHandler()

    @Test
    fun `process() should return domain reminder with SHOPPING type and due date`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, I would like to go on SHOPPING on 31st December")

        val domainReminder = shoppingReminderHandler.process(reminderRequest)

        assertEquals(ReminderType.SHOPPING, domainReminder.type)
        assertEquals(2020, domainReminder.dueDate.year)
        assertEquals(Month.DECEMBER, domainReminder.dueDate.month)
        assertEquals(31, domainReminder.dueDate.dayOfMonth)
    }
}
