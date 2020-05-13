package com.smartcar.voicesystem.reminderservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


@Component
class JWTUtils {
    private val expirationInMillis: Long = 1 * 60 * 60 * 1000L
    private val secret = "voice_system_secret"

    fun createJwt(username: String, vin: String): String {
        val claims = HashMap<String, Any>()
        claims["username"] = username
        claims["vin"] = vin
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(Date(Date().time + TimeUnit.HOURS.toMillis(expirationInMillis)))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
    }

    fun validateToken(authToken: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(authToken)
            true
        } catch (e: SignatureException) {
            false
        }
    }

    fun getAuthentication(token: String?): Authentication? {
        val claims: Claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        val authorities: Collection<GrantedAuthority?> = claims["auth"].toString().split(",".toRegex()).toTypedArray()
                .map { authority -> SimpleGrantedAuthority(authority) }
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }
}