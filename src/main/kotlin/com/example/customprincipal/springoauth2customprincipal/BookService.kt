package com.example.customprincipal.springoauth2customprincipal

interface BookService {
    fun getBooks(userId: Long): List<String>
}
