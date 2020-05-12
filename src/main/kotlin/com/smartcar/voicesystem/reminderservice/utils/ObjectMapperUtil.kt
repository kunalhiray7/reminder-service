package com.smartcar.voicesystem.reminderservice.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.smartcar.voicesystem.reminderservice.serializers.DateTimeDeserializer
import com.smartcar.voicesystem.reminderservice.serializers.DateTimeSerializer
import java.time.ZonedDateTime

object ObjectMapperUtil {

    fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()

        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(ZonedDateTime::class.java, DateTimeSerializer())
        javaTimeModule.addDeserializer(ZonedDateTime::class.java, DateTimeDeserializer())

        objectMapper.registerModule(KotlinModule())
        objectMapper.registerModule(javaTimeModule)

        return objectMapper
    }

}
