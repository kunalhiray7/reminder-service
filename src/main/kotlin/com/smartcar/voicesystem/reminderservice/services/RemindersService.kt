package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import org.springframework.stereotype.Service

@Service
class RemindersService {

    @Throws(ReminderCreateConditionUnmetException::class)
    fun create(reminderRequest: ReminderRequest): Reminder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
