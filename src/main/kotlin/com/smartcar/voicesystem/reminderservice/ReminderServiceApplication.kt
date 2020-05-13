package com.smartcar.voicesystem.reminderservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class ReminderServiceApplication {

	@Bean
	@Primary
	fun serializingObjectMapper(): ObjectMapper {
		return getObjectMapper()
	}
}

fun main(args: Array<String>) {
	runApplication<ReminderServiceApplication>(*args)
}
