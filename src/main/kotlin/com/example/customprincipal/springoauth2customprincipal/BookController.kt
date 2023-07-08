package com.example.customprincipal.springoauth2customprincipal

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/books")
class BookController(private val bookService: BookService) {
    @GetMapping
    fun getBooks(@AuthenticationPrincipal user: CustomOauth2User): List<String> {
        return bookService.getBooks(user.userId)
    }
}
