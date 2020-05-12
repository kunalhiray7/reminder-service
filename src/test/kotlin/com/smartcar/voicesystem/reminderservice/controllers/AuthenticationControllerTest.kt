package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import com.smartcar.voicesystem.reminderservice.controllers.MessageConverter.jacksonDateTimeConverter
import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.AuthenticatedUser
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.services.AuthenticationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
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

    @Test
    fun `POST should return CONFLICT when user already registered`() {
        val registrationRequest = RegistrationRequest(
                username = "john.smith",
                vin = "5LMFU28545LJ68295",
                vehicleMake = "Mercedes",
                vehicleModel = "E-Class"
        )
        doThrow(RegistrationConditionUnmetException("User already registered")).`when`(authenticationService).register(registrationRequest)

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict)
    }

    @Test
    fun `PUT should authenticate the user and return token`() {
        val authRequest =  AuthRequest(username = "john.smith", otp = "9476")
        val authenticatedUser = AuthenticatedUser(username = authRequest.username, accessToken = "ertyui.789.fghjk")
        doReturn(authenticatedUser).`when`(authenticationService).authenticate(authRequest)

        mockMvc.perform(put("/api/authentications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk)
                .andExpect(content().string(mapper.writeValueAsString(authenticatedUser)))

        verify(authenticationService, times(1)).authenticate(authRequest)
    }
}