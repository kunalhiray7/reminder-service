package com.smartcar.voicesystem.reminderservice.domain

import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class Reminder(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @get: NotNull
        val userId: Long = -1L,

        @get: NotBlank
        val reminder: String = "",

        @get: NotNull
        val dueTime: ZonedDateTime = ZonedDateTime.now(UTC),

        @get: NotNull
        val createdAt: ZonedDateTime = ZonedDateTime.now(UTC)
)
