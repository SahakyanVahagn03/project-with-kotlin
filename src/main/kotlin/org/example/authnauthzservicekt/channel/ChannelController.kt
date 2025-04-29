package org.example.authnauthzservicekt.channel

import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.dto.ChannelRequestDto
import org.example.authnauthzservicekt.model.Channel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/channel")
class ChannelController (
    private val channelService: ChannelService,
    private val userService: UserService
){
    @PostMapping("/create-channel")
    fun saveChannel(@RequestBody channelRequest: ChannelRequestDto) {
        channelService.saveChannel(
            Channel(
                name = channelRequest.name,
                description = channelRequest.description,
                teacher = userService.getCurrentUser() ?: throw RuntimeException("No user exists"),
            )
        )
    }

    @GetMapping("/remove-channel/{channelId}")
    fun removeChannel(@PathVariable channelId: Long):List<Channel> {
       return channelService.removeChannel(channelId)
    }
}