package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.Account
import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import com.smartcar.voicesystem.reminderservice.handlers.ReminderTypeHandler
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.repositories.RemindersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
import java.util.*

@ExtendWith(MockitoExtension::class)
class RemindersServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var remindersRepository: RemindersRepository

    @InjectMocks
    private lateinit var remindersService: RemindersService

    @Test
    fun `create() should save the reminder`() {
        val userId = 123L
        val reminderRequest = ReminderRequest(
                userId = userId,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder")
        val reminder = ReminderTypeHandler.getInstance(reminderRequest.reminder).process(reminderRequest)

        val account = Account(id = userId, username = "john.smith", vin = "JT3HN86RXV0067967", vehicleMake = "Mercedes", vehicleModel = "E-Class")
        val argument = ArgumentCaptor.forClass(Reminder::class.java)
        doReturn(reminder).`when`(remindersRepository).save(ArgumentMatchers.any())
        doReturn(Optional.of(account)).`when`(accountRepository).findById(userId)
        val result = remindersService.create(reminderRequest)

        assertEquals(reminder.userId, result.userId)
        assertEquals(reminder.reminder, result.reminder)
        assertEquals(reminder.type, result.type)
        assertEquals(reminder.dueDate, result.dueDate)

        verify(accountRepository, times(1)).findById(userId)
        verify(remindersRepository, times(1)).save(argument.capture())
        assertEquals(0, ChronoUnit.MINUTES.between(argument.value.createdAt, result.createdAt))
    }

    @Test
    fun `getForUser() should return the today's reminders for the given user and categories`() {
        val userId = 123L
        val reminders = listOf(Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.APPOINTMENT,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.SHOPPING,
                reminder = "Hey Mercedes, add a reminder as I want to go for shopping on 13th of November",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.GENERAL,
                reminder = "Hey Mercedes, add football game for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ))
        val currentDate = ZonedDateTime.now(ZoneOffset.UTC)
        val todayStart = ZonedDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 0, 0, 0, 0, ZoneOffset.UTC)
        val categories = listOf(ReminderType.APPOINTMENT, ReminderType.SHOPPING)
        doReturn(reminders).`when`(remindersRepository).findByUserIdAndCreatedAtAfter(userId, todayStart)

        val result = remindersService.getForUser(userId, categories)

        assertEquals(listOf(reminders[0], reminders[1]), result)
        verify(remindersRepository, times(1)).findByUserIdAndCreatedAtAfter(userId, todayStart)
    }

    @Test
    fun `getForUser() should return today's reminders for the given user if no category specified`() {
        val userId = 123L
        val reminders = listOf(Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.APPOINTMENT,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.SHOPPING,
                reminder = "Hey Mercedes, add a reminder as I want to go for shopping on 13th of November",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                type = ReminderType.GENERAL,
                reminder = "Hey Mercedes, add football game for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, ZoneOffset.UTC),
                createdAt = ZonedDateTime.now(ZoneOffset.UTC)
        ))
        val currentDate = ZonedDateTime.now(ZoneOffset.UTC)
        val todayStart = ZonedDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 0, 0, 0, 0, ZoneOffset.UTC)
        doReturn(reminders).`when`(remindersRepository).findByUserIdAndCreatedAtAfter(userId, todayStart)

        val result = remindersService.getForUser(userId, null)

        assertEquals(reminders, result)
        verify(remindersRepository, times(1)).findByUserIdAndCreatedAtAfter(userId, todayStart)
    }

    @Test
    fun `create() should throw ReminderCreateConditionUnmetException when user does not exist for the given reminder`() {
        val userId = 123L
        val reminderRequest = ReminderRequest(userId = userId,
                reminder = "Hey Merc, please add a reminder as an appointment at 10 AM on 3rd June")
        doReturn(Optional.ofNullable(null)).`when`(accountRepository).findById(userId)

        val result = assertThrows<ReminderCreateConditionUnmetException> { remindersService.create(reminderRequest) }

        assertEquals("Not able to create reminder as user: $userId does not exist", result.message)
        verify(accountRepository, times(1)).findById(userId)
        verifyNoInteractions(remindersRepository)
    }
}
