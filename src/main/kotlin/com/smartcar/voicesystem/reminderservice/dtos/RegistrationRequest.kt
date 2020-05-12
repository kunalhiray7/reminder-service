package com.smartcar.voicesystem.reminderservice.dtos

import com.smartcar.voicesystem.reminderservice.domain.Account
import javax.validation.constraints.NotBlank

data class RegistrationRequest(

        @get: NotBlank
        val username: String,

        @get: NotBlank
        val vin: String,

        @get: NotBlank
        val vehicleMake: String,

        @get: NotBlank
        val vehicleModel: String
) {
        fun toDomain() = Account(
                username = username,
                vin = vin,
                vehicleModel = vehicleModel,
                vehicleMake = vehicleMake
        )
}
