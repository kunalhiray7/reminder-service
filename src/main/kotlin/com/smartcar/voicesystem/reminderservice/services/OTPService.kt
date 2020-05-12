package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.repositories.OTPRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class OTPService(private val otpRepository: OTPRepository) {

    fun generate(username: String, vin: String): OTP {
        val randomOtp = generateRandom()
        val otp = OTP(
                username = username,
                otp = randomOtp,
                vin = vin
        )

        return otpRepository.save(otp)
    }

    fun findByUsernameAndVin(username: String, vin: String): OTP? = otpRepository.findByUsernameAndVin(username, vin)

    private fun generateRandom() = String.format("%04d", Random().nextInt(10000))
}
