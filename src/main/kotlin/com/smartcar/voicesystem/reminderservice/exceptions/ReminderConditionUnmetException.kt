package com.smartcar.voicesystem.reminderservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.CONFLICT)
class ReminderCreateConditionUnmetException(override val message: String?): Exception(message)
