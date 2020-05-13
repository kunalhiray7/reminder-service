package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import com.smartcar.voicesystem.reminderservice.handlers.ReminderTypeHandler
import com.smartcar.voicesystem.reminderservice.repositories.RemindersRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RemindersService(private val remindersRepository: RemindersRepository) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RemindersService::class.java)
    }

    @Throws(ReminderCreateConditionUnmetException::class)
    fun create(reminderRequest: ReminderRequest): Reminder {
        LOGGER.info("Creating a reminder for the user with ID ${reminderRequest.userId}")
        val reminder = ReminderTypeHandler.getInstance(reminderRequest.reminder).process(reminderRequest)

        return remindersRepository.save(reminder)
    }

    fun getForUser(userId: Long): List<Reminder> = remindersRepository.findByUserId(userId)

}
