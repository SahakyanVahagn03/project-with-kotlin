package org.example.authnauthzservicekt.joinChannel

import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.model.Channel
import org.example.authnauthzservicekt.model.JoinChannel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/channel")
class JoinChannelController (
    private val joinChannelService: JoinChannelService,
    private val userService: UserService,
){

    @GetMapping("/join/{channelId}")
    fun joinChannelRequest(@PathVariable channelId:Long) {
        joinChannelService.joinRequest(channelId)
    }

    @GetMapping("/approved/{channelId}")
    fun approvedRequest(@PathVariable channelId:Long) {
        joinChannelService.approvedRequest(channelId)
    }

    @GetMapping("/reject/{channelId}")
    fun rejectRequest(@PathVariable channelId:Long) {
        joinChannelService.rejectRequest(channelId)
    }

    @GetMapping("/remove-user/{channelId}")
    fun removeUserRequest(@PathVariable channelId:Long) {
        val joinChannel = joinChannelService
            .findByUserAndChannel(userService.getCurrentUser() ?: throw RuntimeException("current user not found"), channelId)
        joinChannelService.removeUserFromChannel(joinChannel ?: throw RuntimeException("join channel doesn't exist"))
    }

}