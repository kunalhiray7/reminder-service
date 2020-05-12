package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.AuthenticatedUser
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.InvalidCredentialException
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.services.AuthenticationService
import com.smartcar.voicesystem.reminderservice.services.OTPService
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
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

    @Mock
    private lateinit var jwtUtils: JWTUtils

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
        val otp = OTP(otp = "9472", username = registrationRequest.username, vin = registrationRequest.vin)
        val account = registrationRequest.toDomain()
        doReturn(null).`when`(accountRepository).findByUsernameAndVin(registrationRequest.username, registrationRequest.vin)
        doReturn(otp).`when`(otpService).generate(registrationRequest.username, registrationRequest.vin)
        doReturn(account).`when`(accountRepository).save(account)

        val result = authenticationService.register(registrationRequest)

        assertEquals(otp, result)
        verify(accountRepository, times(1)).findByUsernameAndVin(registrationRequest.username, registrationRequest.vin)
        verify(otpService, times(1)).generate(registrationRequest.username, registrationRequest.vin)
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
        doReturn(account).`when`(accountRepository).findByUsernameAndVin(registrationRequest.username, registrationRequest.vin)

        val result = assertThrows<RegistrationConditionUnmetException> { authenticationService.register(registrationRequest) }

        assertEquals("User with username ${registrationRequest.username} and vin ${registrationRequest.vin} is already registered.", result.message)
        verify(accountRepository, times(1)).findByUsernameAndVin(registrationRequest.username, registrationRequest.vin)
        verifyNoInteractions(otpService)
        verifyNoMoreInteractions(accountRepository)
    }

    @Test
    fun `authenticate() should authenticate user with otp and send access token`() {
        val authRequest =  AuthRequest(username = "john.smith", otp = "9476", vin = "5LMFU28545LJ68295")
        val accessToken = "ertyui.789.fghjk"
        val authenticatedUser = AuthenticatedUser(username = authRequest.username, accessToken = accessToken)
        val otp = OTP(username = authRequest.username, otp = authRequest.otp, vin = authRequest.vin)
        doReturn(otp).`when`(otpService).findByUsernameAndVin(authRequest.username, authRequest.vin)
        doReturn(accessToken).`when`(jwtUtils).createJwt(authRequest.username, authRequest.vin)

        val result = authenticationService.authenticate(authRequest)

        assertEquals(authenticatedUser, result)
        verify(otpService, times(1)).findByUsernameAndVin(authRequest.username, authRequest.vin)
        verify(jwtUtils, times(1)).createJwt(authRequest.username, authRequest.vin)
    }

    @Test
    fun `authenticate() should throw InvalidCredentialsException when no credentials found for given username`() {
        val authRequest =  AuthRequest(username = "john.smith", otp = "9476", vin = "5LMFU28545LJ68295")
        doReturn(null).`when`(otpService).findByUsernameAndVin(authRequest.username, authRequest.vin)

        val result = assertThrows<InvalidCredentialException> { authenticationService.authenticate(authRequest) }

        assertEquals("Username or VIN is invalid", result.message)
        verify(otpService, times(1)).findByUsernameAndVin(authRequest.username, authRequest.vin)
        verifyNoInteractions(jwtUtils)
    }

    @Test
    fun `authenticate() should throw InvalidCredentialsException when otp is invalid`() {
        val authRequest =  AuthRequest(username = "john.smith", otp = "9476", vin = "5LMFU28545LJ68295")
        val otp = OTP(username = authRequest.username, otp = "1234", vin = authRequest.vin)
        doReturn(otp).`when`(otpService).findByUsernameAndVin(authRequest.username, authRequest.vin)

        val result = assertThrows<InvalidCredentialException> { authenticationService.authenticate(authRequest) }

        assertEquals("OTP is not valid", result.message)
        verify(otpService, times(1)).findByUsernameAndVin(authRequest.username, authRequest.vin)
        verifyNoInteractions(jwtUtils)
    }
}