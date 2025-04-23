package org.example.authnauthzservicekt.dto


data class ResetPasswordDto(
    val email: String,
    val newPassword: String,
    val verifyCode: String,
)