import org.example.authnauthzservicekt.auth.UserRepository
import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.config.TwilioConfig
import org.example.authnauthzservicekt.dto.ResetPasswordDto
import org.example.authnauthzservicekt.model.ResetOption
import org.example.authnauthzservicekt.model.Role
import org.example.authnauthzservicekt.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*

class UserServiceTest {

    private lateinit var userService: UserService

    private val userRepository = mock(UserRepository::class.java)
    private val jwtTokenProvider = mock(org.example.authnauthzservicekt.auth.JwtTokenProvider::class.java)
    private val passwordEncoder = mock(PasswordEncoder::class.java)
    private val twilioConfig = TwilioConfig()
    private val validator = mock(LocalValidatorFactoryBean::class.java)
    private val emailSender = mock(JavaMailSender::class.java)

    @BeforeEach
    fun setUp() {
        twilioConfig.sid = "dummySid"
        twilioConfig.token = "dummyToken"
        twilioConfig.fromNumber = "+1234567890"

        userService = UserService(
            jwtTokenProvider,
            passwordEncoder,
            userRepository,
            jwtTokenProvider,
            twilioConfig,
            validator,
            emailSender
        )
    }

    @Test
    fun `generateCodeAndSave should return user with verify code`() {
        val email = "test@example.com"
        val user = User(1L, email, "pass", true, "+123456", "", Role.USER)

        `when`(userRepository.findByEmail(email)).thenReturn(user)
        `when`(userRepository.save(any(User::class.java))).thenAnswer { it.arguments[0] }

        val result = userService.generateCodeAndSave(email)

        assertNotNull(result)
        assertEquals(email, result?.email)
        assertTrue(result?.verifyCode?.length == 5)
        verify(userRepository).save(any(User::class.java))
    }

    @Test
    fun `sendEmail should call emailSender send`() {
        val subject = "subject"
        val text = "text"
        val to = "to@example.com"

        userService.sendEmail(subject, text, to)

        verify(emailSender).send(any(SimpleMailMessage::class.java))
    }

    @Test
    fun `sendCodeForVerify with EMAIL should send email`() {
        val email = "user@example.com"
        val user = User(1L, email, "pass", true, "+123456", "", Role.USER)

        `when`(userRepository.findByEmail(email)).thenReturn(user)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        userService.sendCodeForVerify(email, ResetOption.EMAIL)

        verify(emailSender).send(any(SimpleMailMessage::class.java))
    }

    @Test
    fun `resetPasswordWithVerifyCode should update password and clear code`() {
        val email = "user@example.com"
        val code = "12345"
        val newPass = "newPass"
        val encodedPass = "encoded"

        val user = User(1L, email, "oldPass", true, "+123456", code, Role.USER)

        val dto = ResetPasswordDto(email = email, verifyCode = code, newPassword = newPass)

        `when`(userRepository.findByEmail(email)).thenReturn(user)
        `when`(passwordEncoder.encode(newPass)).thenReturn(encodedPass)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        userService.resetPasswordWithVerifyCode(dto)

        assertEquals("", user.verifyCode)
        assertEquals(encodedPass, user.password)
        verify(userRepository).save(user)
    }

    @Test
    fun `resetPasswordWithVerifyCode should throw if code is invalid`() {
        val dto = ResetPasswordDto(email = "user@example.com", verifyCode = "wrong", newPassword = "pass")
        val user = User(1L, dto.email, "pass", true, "+123456", "correct", Role.USER)

        `when`(userRepository.findByEmail(dto.email)).thenReturn(user)

        val exception = assertThrows(RuntimeException::class.java) {
            userService.resetPasswordWithVerifyCode(dto)
        }

        assertEquals("Invalid email or verification code", exception.message)
    }
}
