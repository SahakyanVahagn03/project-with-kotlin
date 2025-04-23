package org.example.authnauthzservicekt.dto

import org.example.authnauthzservicekt.model.Role
import org.example.authnauthzservicekt.model.User
import org.springframework.security.crypto.password.PasswordEncoder

data class AuthorizationRequestDto(
    val email: String,
    val password: String,
    val phoneNumber: String,
)

fun AuthorizationRequestDto.auhZRequestDtoToUser(passwordEncoder: PasswordEncoder): User {
    return User(
        id = 0,
        email = email,
        password = passwordEncoder.encode(password),
        isActive = true,
        role = Role.USER,
        phoneNumber = phoneNumber,
        verifyCode = ""
    )
}