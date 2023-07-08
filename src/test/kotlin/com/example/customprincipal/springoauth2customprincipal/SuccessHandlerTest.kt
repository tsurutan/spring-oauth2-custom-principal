package com.example.customprincipal.springoauth2customprincipal

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import java.lang.Exception

@ExtendWith(MockKExtension::class)
class SuccessHandlerTest {
    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var authentication: OAuth2AuthenticationToken

    lateinit var successHandler: SuccessHandler
    lateinit var request: MockHttpServletRequest
    lateinit var response: MockHttpServletResponse


    @BeforeEach
    fun setup() {
        successHandler = SuccessHandler(userRepository)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        every { authentication.principal.getAttribute<String>("gid") } returns ""
        every { authentication.principal.getAttribute<String>("name") } returns ""
        every { authentication.principal.authorities } returns emptyList()
        every { authentication.authorities } returns emptyList()
        every { authentication.authorizedClientRegistrationId } returns "dummy"
        every { userRepository.findOrSaveByGid(any()) } returns User(userId = 0)
    }

    @Test
    fun `ユーザーを取得または保存する`() {
        every { authentication.principal.getAttribute<String>("gid") } returns "12"
        every { userRepository.findOrSaveByGid("12") } returns User(userId = 12L)

        successHandler.onAuthenticationSuccess(request = request, response = response, authentication = authentication)

        verify { userRepository.findOrSaveByGid("12") }
    }

    @Test
    fun `Principalを更新する`() {
        every { authentication.principal.getAttribute<String>("name") } returns "sample name"
        every { userRepository.findOrSaveByGid(any()) } returns User(userId = 12L)
        every { authentication.principal.authorities } returns emptyList()

        successHandler.onAuthenticationSuccess(request = request, response = response, authentication = authentication)

        val authentication = SecurityContextHolder.getContext().authentication

        assertEquals(CustomOauth2User(userId = 12L, name="sample name", authorities = emptyList()), authentication.principal)
    }

    @Nested
    @DisplayName("エラーケース")
    inner class ErrorCases {

        @Test
        fun `名前がない場合エラーを投げる`() {
            every { authentication.principal.getAttribute<String>("name") } returns null
            val exceptions = assertThrows<Exception> {
                successHandler.onAuthenticationSuccess(request = request, response = response, authentication = authentication)
            }
            assertEquals("name doesn't exist", exceptions.message)
        }

        @Test
        fun `gidがない場合エラーを投げる`() {
            every { authentication.principal.getAttribute<String>("gid") } returns null
            val exceptions = assertThrows<Exception> {
                successHandler.onAuthenticationSuccess(request = request, response = response, authentication = authentication)
            }
            assertEquals("gid doesn't exist", exceptions.message)
        }
    }

}
