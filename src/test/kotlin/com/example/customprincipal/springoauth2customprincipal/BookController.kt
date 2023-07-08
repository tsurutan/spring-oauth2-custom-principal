package com.example.customprincipal.springoauth2customprincipal

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BookController {

    @TestConfiguration
    class Config {
        @Bean
        fun bookService(): BookService {
            return mockk()
        }
        @Bean
        fun userRepository(): UserRepository {
            return mockk()
        }
    }
    @Autowired
    lateinit var mockMVC: MockMvc

    @Test
    fun `sample test`() {

        mockMVC.perform(
                get("/api/users")
                        .with(oauth2Login()
                                .oauth2User(
                                        CustomOauth2User(userId = 1L, name = "sample name", authorities = emptyList())
                                )
                        )
        ).andExpect(status().isOk)
    }
}
