package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.repositories.OTPRepository
import com.smartcar.voicesystem.reminderservice.services.OTPService
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val otpService: OTPService,
                            private val accountRepository: AccountRepository) {

    @Throws(RegistrationConditionUnmetException::class)
    fun register(registrationRequest: RegistrationRequest): OTP {

        val existingAccount = accountRepository.findByUsername(registrationRequest.username)
        if(existingAccount != null) {
            throw RegistrationConditionUnmetException("User with username ${registrationRequest.username} is already registered.")
        }

        val otp = otpService.generate(registrationRequest.username)
        val account = registrationRequest.toDomain()
        accountRepository.save(account)

        return otp
    }

}
