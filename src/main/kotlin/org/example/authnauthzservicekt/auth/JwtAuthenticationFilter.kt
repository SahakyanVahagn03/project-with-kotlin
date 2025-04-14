package org.example.authnauthzservicekt.auth

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter

@org.springframework.stereotype.Component
class JwtAuthenticationFilter (private val jwtTokenProvider: JwtTokenProvider, private val userDetailsService: UserDetailsService) :
    OncePerRequestFilter() {

    @Throws(ServletException::class, java.io.IOException::class) override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: jakarta.servlet.FilterChain) {

        val token = getJwtFromRequest(request)

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val username: String = jwtTokenProvider.getUsername(token)
            val userDetails = userDetailsService.loadUserByUsername(username)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}