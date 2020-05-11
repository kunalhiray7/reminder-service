package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.handlers.ReminderTypeHandler
import com.smartcar.voicesystem.reminderservice.repositories.RemindersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
class RemindersServiceTest {

    @Mock
    private lateinit var remindersRepository: RemindersRepository

    @InjectMocks
    private lateinit var remindersService: RemindersService

    @Test
    fun `create() should save the reminder`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder")
        val reminder = ReminderTypeHandler.getInstance(reminderRequest.reminder).process(reminderRequest)

        val argument = ArgumentCaptor.forClass(Reminder::class.java)
        doReturn(reminder).`when`(remindersRepository).save(ArgumentMatchers.any())

        val result = remindersService.create(reminderRequest)

        assertEquals(reminder.userId, result.userId)
        assertEquals(reminder.reminder, result.reminder)
        assertEquals(reminder.type, result.type)
        assertEquals(reminder.dueDate, result.dueDate)

        verify(remindersRepository, times(1)).save(argument.capture())
        assertEquals(0, ChronoUnit.MINUTES.between(argument.value.createdAt, result.createdAt))
    }

    @Test
    fun `getForUser() should return the reminders for the given user`() {
        val userId = 123L
        val reminders = listOf(Reminder(
                id = 234L,
                userId = userId,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                reminder = "Hey Mercedes, add a reminder as I want to go for shopping on 13th of November",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ))
        doReturn(reminders).`when`(remindersRepository).findByUserId(userId)

        val result = remindersService.getForUser(userId)

        assertEquals(reminders, result)
        verify(remindersRepository, times(1)).findByUserId(userId)
    }
}
