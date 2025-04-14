package org.example.authnauthzservicekt.model

import jakarta.persistence.*
import org.example.authnauthzservicekt.dto.AuthorizationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationResponseDto
import org.mapstruct.Mapping
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "\"user\"")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true)
    val email: String,

    val password: String,

    var isActive: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role

){
    fun User.userToAuhZResponseDto(): AuthorizationResponseDto{
        return AuthorizationResponseDto(
            email = email,
            isActive = isActive,
            role = role
        )
    }

    fun auhZRequestDtoToUser(auhZRequestDto: AuthorizationRequestDto,passwordEncoder: PasswordEncoder): User{
        return User(
            id = 0,
            email = email,
            password = password,
            isActive = true,
            role = Role.USER
        )
    }
}