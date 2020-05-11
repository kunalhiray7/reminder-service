package com.smartcar.voicesystem.reminderservice.handlers

import org.junit.jupiter.api.Test

class ReminderTypeHandlerTest {

    @Test
    fun `should return the correct instance of the handler when reminder contains appointment keyword`() {
        val reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder"

        val instance = ReminderTypeHandler.getInstance(reminder)

        assert(instance is AppointmentReminderHandler)
    }

    @Test
    fun `should return the correct instance of the handler when reminder contains appointments keyword`() {
        val reminder = "Hey Mercedes, add visiting doctor into my Appointments as a reminder"

        val instance = ReminderTypeHandler.getInstance(reminder)

        assert(instance is AppointmentReminderHandler)
    }

    @Test
    fun `should return the correct instance of the handler when reminder contains shopping keyword`() {
        val reminder = "Hey Mercedes, I would like to go on SHOPPING tomorrow please add a reminder"

        val instance = ReminderTypeHandler.getInstance(reminder)

        assert(instance is ShoppingReminderHandler)
    }

    @Test
    fun `should return the correct instance of the handler when reminder contains shop keyword`() {
        val reminder = "Hey Mercedes, I want to shop some clothes next Sunday please add a reminder"

        val instance = ReminderTypeHandler.getInstance(reminder)

        assert(instance is ShoppingReminderHandler)
    }

    @Test
    fun `should return the General handler when reminder type is neither appointment nor shopping`() {
        val reminder = "Hey Mercedes, add a reminder to pay the house rent"

        val instance = ReminderTypeHandler.getInstance(reminder)

        assert(instance is GeneralReminderHandler)
    }
}
