package com.example.customprincipal.springoauth2customprincipal

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class CustomOauth2User(private val authorities: Collection<GrantedAuthority>, val userId: Long, private val name: String): OAuth2User {
    private val attributes: Map<String, Any>

    init {
        attributes = mapOf("userId" to userId, "name" to name)
    }

    override fun getName(): String {
        return name
    }

    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }
}
