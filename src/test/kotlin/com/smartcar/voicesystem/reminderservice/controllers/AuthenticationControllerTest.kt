package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.ObjectMapperUtil.getObjectMapper
import com.smartcar.voicesystem.reminderservice.controllers.MessageConverter.jacksonDateTimeConverter
import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
class AuthenticationControllerTest {

    private val mapper = getObjectMapper()

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var authenticationService: AuthenticationService

    @InjectMocks
    private lateinit var authenticationController: AuthenticationController

    @BeforeEach
    fun setUp() {
            mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                    .setMessageConverters(jacksonDateTimeConverter()).build()
    }

    @Test
    fun `POST should create a registration for the user`() {
        val registrationRequest = RegistrationRequest(
                username = "john.smith",
                vin = "5LMFU28545LJ68295",
                vehicleMake = "Mercedes",
                vehicleModel = "E-Class"
        )
        val otp = OTP(otp = "9472")
        doReturn(otp).`when`(authenticationService).register(registrationRequest)

        mockMvc.perform(post("/api/registrations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated)
                .andExpect(content().string(mapper.writeValueAsString(otp)))

        verify(authenticationService, times(1)).register(registrationRequest)
    }
}