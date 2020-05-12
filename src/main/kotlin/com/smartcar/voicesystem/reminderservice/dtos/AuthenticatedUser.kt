package com.smartcar.voicesystem.reminderservice.dtos

data class AuthenticatedUser(
        val username: String,
        val accessToken: String
)
