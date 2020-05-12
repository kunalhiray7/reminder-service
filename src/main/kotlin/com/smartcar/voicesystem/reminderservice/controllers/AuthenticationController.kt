package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.services.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/api/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid registrationRequest: RegistrationRequest)
            = authenticationService.register(registrationRequest)

    @PutMapping("/api/authentications")
    fun authenticate(@RequestBody @Valid authRequest: AuthRequest) = authenticationService.authenticate(authRequest)

}
