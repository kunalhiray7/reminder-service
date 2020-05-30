package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import com.smartcar.voicesystem.reminderservice.handlers.ReminderTypeHandler
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.repositories.RemindersRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

@Service
class RemindersService(private val remindersRepository: RemindersRepository,
                       private val accountRepository: AccountRepository) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RemindersService::class.java)
    }

    @Throws(ReminderCreateConditionUnmetException::class)
    fun create(reminderRequest: ReminderRequest): Reminder {
        LOGGER.info("Creating a reminder for the user with ID ${reminderRequest.userId}")
        accountRepository.findById(reminderRequest.userId).orElseThrow {
            ReminderCreateConditionUnmetException("Not able to create reminder as user: ${reminderRequest.userId} does not exist")
        }
        val reminder = ReminderTypeHandler.getInstance(reminderRequest.reminder).process(reminderRequest)

        return remindersRepository.save(reminder)
    }

    fun getForUser(userId: Long, categories: List<ReminderType>?): List<Reminder> {
        val currentDate = ZonedDateTime.now(UTC)
        val todayStart = ZonedDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 0, 0, 0, 0, UTC)
        val todayReminders = remindersRepository.findByUserIdAndCreatedAtAfter(userId, todayStart)

        return when (categories) {
            null -> todayReminders
            else -> todayReminders.filter { categories.contains(it.type) }
        }
    }

}
