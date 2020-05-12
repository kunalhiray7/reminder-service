package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.services.OTPService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthenticationServiceTest {

    @Mock
    private lateinit var otpService: OTPService

    @Mock
    private lateinit var accountRepository: AccountRepository

    @InjectMocks
    private lateinit var authenticationService: AuthenticationService

    @Test
    fun `register() should register the user and send otp`() {
        val registrationRequest = RegistrationRequest(
                username = "john.smith",
                vin = "5LMFU28545LJ68295",
                vehicleMake = "Mercedes",
                vehicleModel = "E-Class"
        )
        val otp = OTP(otp = "9472", username = registrationRequest.username)
        val account = registrationRequest.toDomain()
        doReturn(null).`when`(accountRepository).findByUsername(registrationRequest.username)
        doReturn(otp).`when`(otpService).generate(registrationRequest.username)
        doReturn(account).`when`(accountRepository).save(account)

        val result = authenticationService.register(registrationRequest)

        assertEquals(otp, result)
        verify(accountRepository, times(1)).findByUsername(registrationRequest.username)
        verify(otpService, times(1)).generate(registrationRequest.username)
        verify(accountRepository, times(1)).save(account)
    }

    @Test
    fun `register() should throw RegistrationConditionUnmetException when user already exists`() {
        val registrationRequest = RegistrationRequest(
                username = "john.smith",
                vin = "5LMFU28545LJ68295",
                vehicleMake = "Mercedes",
                vehicleModel = "E-Class"
        )
        val account = registrationRequest.toDomain()
        doReturn(account).`when`(accountRepository).findByUsername(registrationRequest.username)

        val result = assertThrows<RegistrationConditionUnmetException> { authenticationService.register(registrationRequest) }

        assertEquals("User with username ${registrationRequest.username} is already registered.", result.message)
        verify(accountRepository, times(1)).findByUsername(registrationRequest.username)
        verifyNoInteractions(otpService)
        verifyNoMoreInteractions(accountRepository)
    }
}