package com.smartcar.voicesystem.reminderservice.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

@Component
class JWTUtils {
    private val expiration: Long = 100L
    private val secret = "voice_system_secret"
    private val header = "Authorization"

    fun createJwt(username: String): String {
        val claims = HashMap<String, Any>()
        claims["username"] = username
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(Date(Date().time + TimeUnit.HOURS.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
    }
}