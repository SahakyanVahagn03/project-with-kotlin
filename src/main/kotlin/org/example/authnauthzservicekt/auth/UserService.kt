package org.example.authnauthzservicekt.auth

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.example.authnauthzservicekt.dto.AuthenticationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationResponseDto
import org.example.authnauthzservicekt.dto.auhZRequestDtoToUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val JwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun register(authZRequestDto: AuthorizationRequestDto): AuthorizationResponseDto =
        userRepository.findByEmail(authZRequestDto.email)
            ?.let { throw RuntimeException("Email already exists") }
            .run {
                authZRequestDto.auhZRequestDtoToUser(passwordEncoder)
                    .let(userRepository::save)
            }.let { user ->
                AuthorizationResponseDto(
                    email = user.email,
                    isActive = user.isActive,
                    role = user.role)
            }

    fun login(request: AuthenticationRequestDto): String =
        userRepository.findByEmail(request.login)?.takeIf {
            passwordEncoder.matches(request.password, it.password)
        }?.let {
            val auth = UsernamePasswordAuthenticationToken(it.email, it.password)
            jwtTokenProvider.generateToken(auth)
        } ?: throw RuntimeException("Invalid credentials or user not found")
}