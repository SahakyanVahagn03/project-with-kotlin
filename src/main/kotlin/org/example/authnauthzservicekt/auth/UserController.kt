package org.example.authnauthzservicekt.auth


import org.example.authnauthzservicekt.dto.AuthenticationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationRequestDto
import org.example.authnauthzservicekt.dto.AuthorizationResponseDto
import org.example.authnauthzservicekt.dto.ResetPasswordDto
import org.example.authnauthzservicekt.model.ResetOption
import org.example.authnauthzservicekt.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody authRequestDto: AuthorizationRequestDto): AuthorizationResponseDto {
        return userService.register(authRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody authNRequest: AuthenticationRequestDto): String {
        return userService.login(authNRequest)
    }

    @GetMapping("/send/recovery-code/{email}/{resetOption}")
    fun sendForRecovery(
        @PathVariable email: String,
        @PathVariable resetOption: String
    ) {
        userService.sendCodeForVerify(email, ResetOption.valueOf(resetOption))
    }

    @PostMapping("/reset-password")
    fun sendForRecovery(
        @RequestBody resetPasswordDto: ResetPasswordDto
    ) {
        userService.resetPasswordWithVerifyCode(resetPasswordDto)
    }
}