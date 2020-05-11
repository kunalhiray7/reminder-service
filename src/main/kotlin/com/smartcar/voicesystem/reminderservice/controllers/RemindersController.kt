package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.services.RemindersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class RemindersController(private val remindersService: RemindersService) {

    @PostMapping("/api/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid reminderRequest: ReminderRequest) = remindersService.create(reminderRequest)

    @GetMapping("/api/reminders")
    fun getForUser(@RequestParam(value = "userId", required = true) userId: Long) = remindersService.getForUser(userId)

}
