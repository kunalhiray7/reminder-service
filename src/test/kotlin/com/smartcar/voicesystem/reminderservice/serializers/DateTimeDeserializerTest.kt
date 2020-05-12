package com.smartcar.voicesystem.reminderservice.serializers

import com.smartcar.voicesystem.reminderservice.utils.ObjectMapperUtil.getObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

class DateTimeDeserializerTest {

    private val mapper = getObjectMapper()

    @Test
    fun `serialize() should deserialize the ZonedDateTime`() {
        assertEquals(ZonedDateTime.of(2020, 12, 31, 12, 59, 59, 0, UTC), mapper.readValue(""""2020-12-31T12:59:59Z"""",
                ZonedDateTime::class.java))
    }
}
