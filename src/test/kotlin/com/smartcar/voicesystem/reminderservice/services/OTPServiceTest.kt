package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.repositories.OTPRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OTPServiceTest {

    @Mock
    private lateinit var otpRepository: OTPRepository

    @InjectMocks
    private lateinit var otpService: OTPService

    @Test
    fun `generate() should generate random OTP and save it`() {
        val otp = OTP(otp = "9472", username = "username")
        val captor = ArgumentCaptor.forClass(OTP::class.java)
        doReturn(otp).`when`(otpRepository).save(ArgumentMatchers.any())

        val result = otpService.generate(otp.username)

        assertEquals(otp, result)
        verify(otpRepository, times(1)).save(captor.capture())
        assertNotEquals(otp.otp, captor.value.otp)
    }
}