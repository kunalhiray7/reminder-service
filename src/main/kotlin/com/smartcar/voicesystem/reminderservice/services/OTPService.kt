package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.repositories.OTPRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class OTPService(private val otpRepository: OTPRepository) {

    fun generate(username: String): OTP {
        val randomOtp = generateRandom()
        val otp = OTP(
                username = username,
                otp = randomOtp
        )

        return otpRepository.save(otp)
    }

    private fun generateRandom() = String.format("%04d", Random().nextInt(10000))
}
