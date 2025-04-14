package org.example.authnauthzservicekt.dto

import org.example.authnauthzservicekt.model.Role

data class AuthorizationResponseDto(
    val email: String,
    val isActive: Boolean,
    val role: Role
)