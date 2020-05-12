package com.smartcar.voicesystem.reminderservice.repositories

import com.smartcar.voicesystem.reminderservice.domain.OTP
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OTPRepository: JpaRepository<OTP, Long> {
    fun findByUsernameAndVin(username: String, vin: String): OTP?
}
