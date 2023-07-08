package com.example.customprincipal.springoauth2customprincipal

interface UserRepository {
    fun findOrSaveByGid(gid: String): User
}
