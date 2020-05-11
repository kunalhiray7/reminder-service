package com.smartcar.voicesystem.reminderservice.handlers

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest

interface ReminderTypeHandler {

    companion object {

        fun getInstance(reminder: String): ReminderTypeHandler {
            val appointTypeKeywords = arrayOf("appointment", "appointments")
            val shoppingTypeKeywords = arrayOf("shopping", "shop")

            val tokens = reminder.split(" ")

            return when {
                tokens.any { appointTypeKeywords.contains(it.toLowerCase()) } -> AppointmentReminderHandler()
                tokens.any { shoppingTypeKeywords.contains(it.toLowerCase()) } -> ShoppingReminderHandler()
                else -> GeneralReminderHandler()
            }
        }

    }

    fun process(reminderRequest: ReminderRequest): Reminder
}
