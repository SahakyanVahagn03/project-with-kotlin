package org.example.authnauthzservicekt.channel

import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.model.Channel
import org.example.authnauthzservicekt.teacher.BecomeTeacherRequestRepository
import org.springframework.stereotype.Service

@Service
class ChannelService(
    val channelRepository: ChannelRepository,
    val userService: UserService
) {

    fun saveChannel(channel: Channel) {
        channel.teacher = userService.getCurrentUser() ?: throw RuntimeException("current User not found")
        channelRepository.save(channel)
    }

    fun removeChannel(channelId:Long):List<Channel>{
        findByChannelId(channelId).apply {
            isActive = false
        }.let {
            saveChannel(it)
            return channelRepository.findAll().toList()
        }
    }

    fun findByChannelId(channelId:Long): Channel =
         channelRepository.findById(channelId).orElseThrow{
            RuntimeException("User not found")
        }.let {
            when(it.isActive){
                true -> return it
                else -> throw RuntimeException("User not found")
            }
         }
}