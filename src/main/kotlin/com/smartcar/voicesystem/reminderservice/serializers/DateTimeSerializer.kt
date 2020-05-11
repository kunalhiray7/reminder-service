package com.smartcar.voicesystem.reminderservice.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.ZonedDateTime

class DateTimeSerializer: JsonSerializer<ZonedDateTime>() {
    override fun serialize(value: ZonedDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.toString())
    }
}
