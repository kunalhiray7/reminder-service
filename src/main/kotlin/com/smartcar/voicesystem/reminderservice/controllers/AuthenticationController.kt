package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/api/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid registrationRequest: RegistrationRequest) = authenticationService.register(registrationRequest)

}
