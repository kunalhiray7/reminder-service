package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import com.smartcar.voicesystem.reminderservice.controllers.MessageConverter.jacksonDateTimeConverter
import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.exceptions.ReminderCreateConditionUnmetException
import com.smartcar.voicesystem.reminderservice.services.RemindersService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

@SpringBootTest
class RemindersControllerTest {

    private val mapper = getObjectMapper()

    @Mock
    private lateinit var remindersService: RemindersService

    @InjectMocks
    private lateinit var remindersController: RemindersController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(remindersController).setMessageConverters(jacksonDateTimeConverter()).build()
    }

    @Test
    fun `POST should add a reminder for the user`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder")
        val reminder = Reminder(
                id = 234L,
                userId = reminderRequest.userId,
                reminder = reminderRequest.reminder,
                dueDate = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, UTC),
                createdAt = ZonedDateTime.now(UTC)
        )
        doReturn(reminder).`when`(remindersService).create(reminderRequest)

        mockMvc.perform(post("/api/reminders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(reminderRequest)))
                .andExpect(status().isCreated)
                .andExpect(content().string(mapper.writeValueAsString(reminder)))

        verify(remindersService, times(1)).create(reminderRequest)
    }

    @Test
    fun `POST should return CONFLICT when user does not exist for given reminder`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder")
        doThrow(ReminderCreateConditionUnmetException("Not able to create a reminder as user does not exist"))
                .`when`(remindersService).create(reminderRequest)

        mockMvc.perform(post("/api/reminders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(reminderRequest)))
                .andExpect(status().isConflict)

        verify(remindersService, times(1)).create(reminderRequest)
    }

    @Test
    fun `POST should return BAD_REQUEST when reminder request is not valid`() {
        val reminderRequest = ReminderRequest(
                userId = 123L,
                reminder = "")

        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reminderRequest)))
                .andExpect(status().isBadRequest)

        verifyNoInteractions(remindersService)
    }

    @Test
    fun `GET should return the reminders for the user`() {
        val userId = 123L
        val reminders = listOf(Reminder(
                id = 234L,
                userId = userId,
                reminder = "Hey Mercedes, add a new dentist appointment for 10th of November as my reminder",
                dueDate = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, UTC),
                createdAt = ZonedDateTime.now(UTC)
        ), Reminder(
                id = 234L,
                userId = userId,
                reminder = "Hey Mercedes, add a reminder as I want to go for shopping on 13th of November",
                dueDate = ZonedDateTime.of(2020, 9, 13, 0, 0, 0, 0, UTC),
                createdAt = ZonedDateTime.now(UTC)
        ))
        doReturn(reminders).`when`(remindersService).getForUser(userId)

        mockMvc.perform(get("/api/reminders?userId=$userId"))
                .andExpect(status().isOk)
                .andExpect(content().string(mapper.writeValueAsString(reminders)))

        verify(remindersService, times(1)).getForUser(userId)
    }

    @Test
    fun `GET should return BAD_REQUEST when userId not provided as request parameter`() {

        mockMvc.perform(get("/api/reminders"))
                .andExpect(status().isBadRequest)

        verifyNoInteractions(remindersService)
    }
}
