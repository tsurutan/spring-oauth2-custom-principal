package com.example.customprincipal.springoauth2customprincipal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Autowired
    lateinit var successHandler: SuccessHandler

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.oauth2Login {
            it.successHandler(successHandler)
        }
        return http.build()
    }
}
