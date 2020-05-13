package com.smartcar.voicesystem.reminderservice.utils

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.User

class JWTUtilsTest {

    private val jwtUtils = JWTUtils()

    @Test
    fun `should generate a valid jwt token`() {
        val username = "john.smith"
        val vin = "5LMFU28545LJ68295"

        val token = jwtUtils.createJwt(username, vin)

        assert(jwtUtils.validateToken(token))
    }

    @Test
    fun `should have username and vin in the token`() {
        val username = "john.smith"
        val vin = "5LMFU28545LJ68295"

        val token = jwtUtils.createJwt(username, vin)
        val claims = Jwts.parser().setSigningKey("voice_system_secret").parseClaimsJws(token).body

        assertEquals(username, claims["username"])
        assertEquals(vin, claims["vin"])
    }

    @Test
    fun `should return false when token is not valid`() {
        val invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        assertFalse(jwtUtils.validateToken(invalidToken))
    }

    @Test
    fun `should return the authentication for valid token`() {
        val username = "john.smith"
        val vin = "5LMFU28545LJ68295"
        val token = jwtUtils.createJwt(username, vin)

        val result = jwtUtils.getAuthentication(token)

        val user = result?.principal as User
        assertEquals(username, user.username)
    }
}