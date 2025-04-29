package org.example.authnauthzservicekt.auth

import org.example.authnauthzservicekt.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}