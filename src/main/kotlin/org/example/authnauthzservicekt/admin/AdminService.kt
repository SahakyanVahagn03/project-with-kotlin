package org.example.authnauthzservicekt.admin

import org.example.authnauthzservicekt.auth.UserRepository
import org.example.authnauthzservicekt.model.Role
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userRepository: UserRepository
) {

    fun remove(id: Long) {
        userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
            .let {
                it.isActive = false
                userRepository.save(it)
            }
    }

    fun changeRoleToAdmin(id: Long) {
        userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
            .let {
                it.role = Role.ADMIN
                userRepository.save(it)
            }
    }

}