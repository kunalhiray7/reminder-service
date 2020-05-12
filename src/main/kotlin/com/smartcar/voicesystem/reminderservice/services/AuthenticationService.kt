package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.AuthenticatedUser
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.InvalidCredentialException
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.repositories.OTPRepository
import com.smartcar.voicesystem.reminderservice.services.OTPService
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val otpService: OTPService,
                            private val accountRepository: AccountRepository,
                            private val jwtUtils: JWTUtils) {

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

    fun authenticate(authRequest: AuthRequest): AuthenticatedUser {
        val savedOtp = otpService.findByUsername(authRequest.username)
                ?: throw InvalidCredentialException("Username ${authRequest.username} is invalid")

        if(savedOtp.otp == authRequest.otp) {
            return AuthenticatedUser(username = authRequest.username, accessToken = jwtUtils.createJwt(authRequest.username))
        } else {
            throw InvalidCredentialException("OTP is not valid")
        }
    }

}
