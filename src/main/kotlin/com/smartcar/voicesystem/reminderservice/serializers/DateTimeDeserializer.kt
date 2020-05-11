package com.smartcar.voicesystem.reminderservice.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.ZonedDateTime

class DateTimeDeserializer: JsonDeserializer<ZonedDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ZonedDateTime {
        return ZonedDateTime.parse(p?.valueAsString)
    }
}
