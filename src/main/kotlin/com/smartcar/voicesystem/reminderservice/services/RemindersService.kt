package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import com.smartcar.voicesystem.reminderservice.handlers.ReminderTypeHandler
import com.smartcar.voicesystem.reminderservice.repositories.RemindersRepository
import org.springframework.stereotype.Service

@Service
class RemindersService(private val remindersRepository: RemindersRepository) {

    @Throws(ReminderCreateConditionUnmetException::class)
    fun create(reminderRequest: ReminderRequest): Reminder {
        val reminder = ReminderTypeHandler.getInstance(reminderRequest.reminder).process(reminderRequest)

        return remindersRepository.save(reminder)
    }

    fun getForUser(userId: Long): List<Reminder> = remindersRepository.findByUserId(userId)

}
