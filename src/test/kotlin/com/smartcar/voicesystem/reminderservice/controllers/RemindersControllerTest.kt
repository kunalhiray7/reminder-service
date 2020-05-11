package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.ObjectMapperUtil.getObjectMapper
import com.smartcar.voicesystem.reminderservice.controllers.MessageConverter.jacksonDateTimeConverter
import com.smartcar.voicesystem.reminderservice.domain.Reminder
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
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
                dueTime = ZonedDateTime.of(2020, 9, 10, 0, 0, 0, 0, UTC),
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
}
