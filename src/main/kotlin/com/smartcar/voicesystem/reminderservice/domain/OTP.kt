package com.smartcar.voicesystem.reminderservice.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class OTP(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @get: NotBlank
        val username: String = "",

        @get: NotBlank
        val otp: String = "",

        @get: NotBlank
        val vin: String
)
