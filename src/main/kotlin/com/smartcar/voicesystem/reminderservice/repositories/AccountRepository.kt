package com.smartcar.voicesystem.reminderservice.repositories

import com.smartcar.voicesystem.reminderservice.domain.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByUsernameAndVin(username: String, vin: String): Account?
}
