package org.example.authnauthzservicekt.auth
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService (
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)
            ?.let {
                org.springframework.security.core.userdetails.User(
                    it.email,
                    it.password,
                    listOf(SimpleGrantedAuthority(it.role.name))
                )
            } ?: throw UsernameNotFoundException("User not found with username: $username")
}