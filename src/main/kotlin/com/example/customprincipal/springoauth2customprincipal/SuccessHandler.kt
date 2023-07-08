package com.example.customprincipal.springoauth2customprincipal

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class SuccessHandler(private val userRepository: UserRepository): AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        val tokenAuthentication = authentication as OAuth2AuthenticationToken
        val principal = authentication.principal

        val gid = principal.getAttribute<String>("gid") ?: throw Exception("gid doesn't exist")
        val name = principal.getAttribute<String>("name") ?: throw Exception("name doesn't exist")

        val user = userRepository.findOrSaveByGid(gid)
        SecurityContextHolder.getContext().authentication = OAuth2AuthenticationToken(
                CustomOauth2User(authorities = principal.authorities, userId = user.userId, name = name),
                tokenAuthentication.authorities,
                tokenAuthentication.authorizedClientRegistrationId
        )
    }
}
