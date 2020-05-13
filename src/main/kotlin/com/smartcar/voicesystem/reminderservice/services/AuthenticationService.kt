package com.smartcar.voicesystem.reminderservice.services

import com.smartcar.voicesystem.reminderservice.domain.OTP
import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.AuthenticatedUser
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.exceptions.InvalidCredentialException
import com.smartcar.voicesystem.reminderservice.exceptions.RegistrationConditionUnmetException
import com.smartcar.voicesystem.reminderservice.repositories.AccountRepository
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val otpService: OTPService,
                            private val accountRepository: AccountRepository,
                            private val jwtUtils: JWTUtils) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthenticationService::class.java)
    }

    @Throws(RegistrationConditionUnmetException::class)
    fun register(registrationRequest: RegistrationRequest): OTP {

        val existingAccount = accountRepository.findByUsernameAndVin(registrationRequest.username, registrationRequest.vin)
        if(existingAccount != null) {
            val message = "User with username ${registrationRequest.username} and vin ${registrationRequest.vin} is already registered."
            LOGGER.info(message)
            throw RegistrationConditionUnmetException(message)
        }

        val otp = otpService.generate(registrationRequest.username, registrationRequest.vin)
        LOGGER.info("OTP is generated for the user ${registrationRequest.username}")
        val account = registrationRequest.toDomain()
        accountRepository.save(account)
        LOGGER.info("Registration is done for the user ${registrationRequest.username}")
        return otp
    }

    fun authenticate(authRequest: AuthRequest): AuthenticatedUser {
        val savedOtp = otpService.findByUsernameAndVin(authRequest.username, authRequest.vin)
                ?: throw InvalidCredentialException("Username or VIN is invalid")

        if(savedOtp.otp == authRequest.otp) {
            LOGGER.info("OTP is valid, generating access token.")
            return AuthenticatedUser(username = authRequest.username,
                    accessToken = jwtUtils.createJwt(authRequest.username, authRequest.vin))
        } else {
            LOGGER.info("Invalid OTP.")
            throw InvalidCredentialException("OTP is not valid")
        }
    }

}
