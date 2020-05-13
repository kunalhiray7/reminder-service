package com.smartcar.voicesystem.reminderservice.config

import com.smartcar.voicesystem.reminderservice.filters.JwtFilter
import com.smartcar.voicesystem.reminderservice.utils.JWTUtils
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(private val jwtUtils: JWTUtils): WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(jwtUtils)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/registrations").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/authentications").permitAll()
                .anyRequest().fullyAuthenticated()
                .and().logout().permitAll()
        http.csrf().disable()
    }

    override fun configure(web: WebSecurity?) {
        super.configure(web)
        web?.ignoring()?.antMatchers(HttpMethod.POST,"/api/registrations")
                ?.antMatchers(HttpMethod.PUT,"/api/authentications")
                ?.antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
                ?.antMatchers("/actuator/*")
    }
}