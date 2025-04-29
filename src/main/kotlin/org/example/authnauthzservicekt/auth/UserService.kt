package org.example.authnauthzservicekt.auth

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import org.example.authnauthzservicekt.config.TwilioConfig
import org.example.authnauthzservicekt.dto.*
import org.example.authnauthzservicekt.model.ResetOption
import org.example.authnauthzservicekt.model.User
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val JwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val twilioConfig: TwilioConfig,
    private val emailSender: JavaMailSender,



) {
    @PostConstruct
    fun int(){
        Twilio.init(twilioConfig.sid,twilioConfig.token)
    }

    fun generateCodeAndSave(email: String): User? {
        userRepository.findByEmail(email)?.let { user ->
            val verifyCode = UUID.randomUUID().toString().replace("-", "").take(5)
            user.verifyCode = verifyCode
            userRepository.save(user)
            return user
        }
        return null
    }

    fun sendCodeForVerify(email:String,option: ResetOption){
        generateCodeAndSave(email)?.let {
            when (option) {
                ResetOption.SMS -> {
                    sendSmsForReset(it.phoneNumber, it.verifyCode)
                }
                ResetOption.EMAIL -> {
                        sendEmailForReset(it.email,it.verifyCode)
                }
            }
        }
    }

    fun sendEmailForReset(mail:String,verifyCode:String){
        sendEmail("for reset password",verifyCode,mail)
    }

    fun sendEmail(
        subject: String,
        text: String,
        targetEmail: String
    ) {
        val message = SimpleMailMessage()
        message.setSubject(subject)
        message.setText(text)
        message.setTo(targetEmail)
        emailSender.send(message)
    }

    fun getUserById(id: Long) : User {
        userRepository.findById(id).orElseThrow {
            RuntimeException("User not found")
        }.let {
            when(it.isActive){
                true -> return it
                else -> throw RuntimeException("User ${it.id} is not active")
            }
        }
    }

    fun getCurrentUser() : User? {
        val authentication = SecurityContextHolder.getContext()
            .authentication.principal
        val userDetails = authentication as UserDetails
        return userRepository.findByEmail(userDetails.username)
    }

    fun sendSmsForReset(to:String,text:String) {
        Message.creator(
            PhoneNumber(to),
            PhoneNumber(twilioConfig.fromNumber),
            text
        ).create()
    }

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

    fun resetPasswordWithVerifyCode(resetPasswordDto: ResetPasswordDto) {
        userRepository.findByEmail(resetPasswordDto.email)
            ?.takeIf { resetPasswordDto.verifyCode == it.verifyCode
                        && resetPasswordDto.verifyCode != "" }
            ?.apply { password = passwordEncoder.encode(resetPasswordDto.newPassword)}
            ?.let {
                it.verifyCode = ""
                userRepository.save(it)
            }
            ?: throw RuntimeException("Invalid email or verification code")
    }
}