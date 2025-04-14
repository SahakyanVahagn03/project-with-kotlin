package org.example.authnauthzservicekt.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration}")
    private var jwtExpirationDate: Long = 0

    fun generateToken(authentication: Authentication): String {
        val username = authentication.name
        val currentDate = Date()
        val expireDate = Date(currentDate.time + jwtExpirationDate)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(currentDate)
            .setExpiration(expireDate)
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun key(): Key {
        return Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun getUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}
