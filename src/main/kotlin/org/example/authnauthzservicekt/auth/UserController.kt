package org.example.authnauthzservicekt.auth


import org.example.authnauthzservicekt.dto.AuthenticationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationResponseDto
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class UserController (private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody authRequestDto: AuthorizationRequestDto): AuthorizationResponseDto {
        return userService.register(authRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody authNRequest : AuthenticationRequestDto): String{
        return userService.login(authNRequest)
    }

}