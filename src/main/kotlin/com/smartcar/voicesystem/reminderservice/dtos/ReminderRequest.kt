package com.smartcar.voicesystem.reminderservice.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ReminderRequest(

        @get: NotNull
        val userId: Long,

        @get: NotBlank
        val reminder: String
)
