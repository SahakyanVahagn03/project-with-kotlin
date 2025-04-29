package org.example.authnauthzservicekt.admin

import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val adminService: AdminService,
    private val userService: UserService
) {

    @GetMapping("/remove/user/{id}")
    fun removeUser(@PathVariable id: Long){
        adminService.remove(id)
    }

    @GetMapping("/user/{id}")
    fun getUserById(@PathVariable id: Long) : User {
        return userService.getUserById(id)
    }

    @GetMapping("/change/role-admin/{id}")
    fun changeRole(@PathVariable id: Long){
        adminService.changeRoleToAdmin(id)
    }
}