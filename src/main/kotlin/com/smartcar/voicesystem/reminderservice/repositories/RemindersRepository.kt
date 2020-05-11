package com.smartcar.voicesystem.reminderservice.repositories

import com.smartcar.voicesystem.reminderservice.domain.Reminder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RemindersRepository: JpaRepository<Reminder, Long>
