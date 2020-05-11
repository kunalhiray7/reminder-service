package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.services.RemindersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RemindersController(private val remindersService: RemindersService) {

    @PostMapping("/api/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid reminderRequest: ReminderRequest) = remindersService.create(reminderRequest)

}
