package com.smartcar.voicesystem.reminderservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
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

	@Bean
	fun webServerFactory(): ConfigurableServletWebServerFactory? {
		val factory = TomcatServletWebServerFactory()
		factory.addConnectorCustomizers(TomcatConnectorCustomizer { connector -> connector.setProperty("relaxedQueryChars", "|{}[]") })
		return factory
	}
}

fun main(args: Array<String>) {
	runApplication<ReminderServiceApplication>(*args)
}
