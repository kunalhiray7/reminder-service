package com.smartcar.voicesystem.reminderservice.filters

import com.smartcar.voicesystem.reminderservice.AppConstants
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtFilter(private val jwtUtils: JWTUtils): GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            val httpServletRequest: HttpServletRequest = request as HttpServletRequest
            val token = resolveToken(httpServletRequest)
            if(token != null && jwtUtils.validateToken(token)) {
                val authentication: Authentication = jwtUtils.getAuthentication(token)!!
                SecurityContextHolder.getContext().authentication = authentication
            } else {
                (response as HttpServletResponse).status = HttpServletResponse.SC_UNAUTHORIZED
            }

            chain?.doFilter(request, response)
            resetAuthenticationAfterRequest()
        } catch (e: Exception) {
            (response as HttpServletResponse).status = HttpServletResponse.SC_UNAUTHORIZED
        }

    }

    private fun resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().authentication = null
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AppConstants.AUTH_HEADER)
        return if (bearerToken != null && bearerToken.startsWith(AppConstants.BEARER_PREFIX)) {
            bearerToken.substring(AppConstants.BEARER_PREFIX.length, bearerToken.length)
        } else null
    }
}