package com.smartcar.voicesystem.reminderservice.dtos

import javax.validation.constraints.NotBlank

data class AuthRequest(

        @get: NotBlank
        val username: String,

        @get: NotBlank
        val otp: String
)