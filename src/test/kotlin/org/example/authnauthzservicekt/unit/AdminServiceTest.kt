import org.example.authnauthzservicekt.auth.UserRepository
import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.config.TwilioConfig
import org.example.authnauthzservicekt.dto.AuthorizationRequestDto
import org.example.authnauthzservicekt.dto.ResetPasswordDto
import org.example.authnauthzservicekt.model.ResetOption
import org.example.authnauthzservicekt.model.Role
import org.example.authnauthzservicekt.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.authnauthzservicekt.admin.AdminService
import org.example.authnauthzservicekt.dto.AuthenticationRequestDto
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*

class AdminServiceTest {


    private lateinit var userRepository: UserRepository
    private lateinit var adminService: AdminService

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        adminService = AdminService(userRepository)
    }

    @Test
    fun `remove should deactivate user if exists`() {
        val user = User(id = 1L, email = "test@mail.com",isActive = true,phoneNumber = "094902128", verifyCode = "", password = "123", role = Role.USER)
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { userRepository.save(any()) } returns user.copy(isActive = false)

        adminService.remove(1L)

        assertFalse(user.isActive)
        verify { userRepository.save(match { !it.isActive }) }
    }

    @Test
    fun `remove should throw exception if user not found`() {
        every { userRepository.findById(2L) } returns Optional.empty()

        val exception = assertThrows(RuntimeException::class.java) {
            adminService.remove(2L)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `changeRoleToAdmin should update user role to ADMIN`() {
        val user = User(id = 1L, email = "test@mail.com", password = "123", role = Role.USER, verifyCode = "", phoneNumber = "094902128",isActive = true)
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { userRepository.save(any()) } returns user.copy(role = Role.ADMIN)

        adminService.changeRoleToAdmin(1L)

        assertEquals(Role.ADMIN, user.role)
        verify { userRepository.save(match { it.role == Role.ADMIN }) }
    }

    @Test
    fun `changeRoleToAdmin should throw exception if user not found`() {
        every { userRepository.findById(3L) } returns Optional.empty()

        val exception = assertThrows(RuntimeException::class.java) {
            adminService.changeRoleToAdmin(3L)
        }

        assertEquals("User not found", exception.message)
    }
}
