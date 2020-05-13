package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.dtos.AuthRequest
import com.smartcar.voicesystem.reminderservice.dtos.RegistrationRequest
import com.smartcar.voicesystem.reminderservice.services.AuthenticationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(value = "User registration", description = "Endpoints for registering and authenticating the user")
@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @ApiOperation(value = "Register User with VIN and username")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "User registered successfully, returns OTP"),
        ApiResponse(code = 400, message = "Request body is invalid")
    ]
    )
    @PostMapping("/api/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid registrationRequest: RegistrationRequest) = authenticationService.register(registrationRequest)

    @ApiOperation(value = "Authenticate User with VIN, OTP and username")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "User authenticated successfully, returns access token"),
        ApiResponse(code = 400, message = "Request body is invalid"),
        ApiResponse(code = 401, message = "OTP / VIN/ Username is invalid")
    ]
    )
    @PutMapping("/api/authentications")
    fun authenticate(@RequestBody @Valid authRequest: AuthRequest) = authenticationService.authenticate(authRequest)

}
