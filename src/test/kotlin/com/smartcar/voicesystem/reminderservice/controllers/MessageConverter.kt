package com.smartcar.voicesystem.reminderservice.controllers

import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

object MessageConverter {

    fun jacksonDateTimeConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = getObjectMapper()
        return converter
    }

}
