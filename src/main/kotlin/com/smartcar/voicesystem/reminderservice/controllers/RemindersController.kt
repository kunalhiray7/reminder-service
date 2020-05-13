package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.domain.ReminderType
import com.smartcar.voicesystem.reminderservice.dtos.ReminderRequest
import com.smartcar.voicesystem.reminderservice.services.RemindersService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(value = "Manage user's reminders", description = "Endpoints for managing the reminders of the user")
@RestController
class RemindersController(private val remindersService: RemindersService) {

    @ApiOperation(value = "Create a reminder")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Reminder is created for the user"),
        ApiResponse(code = 400, message = "Request body is invalid"),
        ApiResponse(code = 401, message = "No or invalid access token in the request"),
        ApiResponse(code = 409, message = "User does not exists for the given reminder request")
    ]
    )
    @PostMapping("/api/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid reminderRequest: ReminderRequest) = remindersService.create(reminderRequest)

    @ApiOperation(value = "Get all reminders of the user on the current day and for the given categories")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns the list of reminders of the user or empty list if no reminders found"),
        ApiResponse(code = 400, message = "If userId is missing in the request parameters"),
        ApiResponse(code = 401, message = "No or invalid access token in the request")
    ]
    )
    @GetMapping("/api/reminders")
    fun getForUser(@RequestParam(value = "userId", required = true) userId: Long,
                   @RequestParam("category[]", required = false) categories: List<ReminderType>?)
            = remindersService.getForUser(userId, categories)

}
