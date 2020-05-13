package com.smartcar.voicesystem.reminderservice.filters

import com.smartcar.voicesystem.reminderservice.AppConstants
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.Authentication
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
class JwtFilterTest {

    @Mock
    private lateinit var jwtUtils: JWTUtils

    @InjectMocks
    private lateinit var jwtFilter: JwtFilter

    @Test
    fun `should validate token in the request`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLnNtaXRoIiwidmluIjoiNUxNRlUyODU0NUxKNjgyOTUiLCJleHAiOjE0NTQ5Mjk4NjQwLCJ1c2VybmFtZSI6ImpvaG4uc21pdGgifQ.fmRyvqIdLapjeFIt6tDqBCKW9vlWSCa7qxUuFjJRDYI"
        val bearerToken = "Bearer $token"
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)
        val authentication = mock(Authentication::class.java)

        doReturn(bearerToken).`when`(request).getHeader(AppConstants.AUTH_HEADER)
        doReturn(true).`when`(jwtUtils).validateToken(token)
        doReturn(authentication).`when`(jwtUtils).getAuthentication(token)

        jwtFilter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        verify(request, times(1)).getHeader(AppConstants.AUTH_HEADER)
        verify(jwtUtils, times(1)).validateToken(token)
        verify(jwtUtils, times(1)).getAuthentication(token)
    }

    @Test
    fun `should return unauthorized when token is invalid`() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLnNtaXRoIiwidmluIjoiNUxNRlUyODU0NUxKNjgyOTUiLCJleHAiOjE0NTQ5Mjk4NjQwLCJ1c2VybmFtZSI6ImpvaG4uc21pdGgifQ.fmRyvqIdLapjeFIt6tDqBCKW9vlWSCa7qxUuFjJRDYI"
        val bearerToken = "Bearer $token"
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)

        doReturn(bearerToken).`when`(request).getHeader(AppConstants.AUTH_HEADER)
        doReturn(false).`when`(jwtUtils).validateToken(token)

        jwtFilter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        verify(request, times(1)).getHeader(AppConstants.AUTH_HEADER)
        verify(jwtUtils, times(1)).validateToken(token)
    }
}