package com.smartcar.voicesystem.reminderservice.handlers

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.parsers.NaturalLanguageDateParser
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

class AppointmentReminderHandler : ReminderTypeHandler {

    override fun process(reminderRequest: ReminderRequest) = Reminder(
            userId = reminderRequest.userId,
            reminder = reminderRequest.reminder,
            type = ReminderType.APPOINTMENT,
            dueDate = NaturalLanguageDateParser.parse(reminderRequest.reminder),
            createdAt = ZonedDateTime.now(UTC)
    )

}
