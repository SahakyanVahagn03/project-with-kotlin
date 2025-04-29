package org.example.authnauthzservicekt.joinChannel

import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.channel.ChannelService
import org.example.authnauthzservicekt.model.JoinChannel
import org.example.authnauthzservicekt.model.RequestStatus
import org.example.authnauthzservicekt.model.User
import org.springframework.stereotype.Service

@Service
class JoinChannelService(
    val joinChannelRepository: JoinChannelRepository,
    val channelService: ChannelService,
    val userService: UserService,
) {

    fun joinRequest(channelId: Long): JoinChannel {
        val joinChannel = findByUserAndChannel(userService.getCurrentUser()  ?: throw RuntimeException("current user not found"), channelId)
        if (joinChannel?.requestStatus == RequestStatus.PENDING) {
            return joinChannel
        }
        return joinChannelRepository.save(
            JoinChannel(
                channel = channelService.findByChannelId(channelId),
                user = userService.getCurrentUser()  ?: throw RuntimeException("current user not found")
            )
        )
    }

    fun findById(joinChannelId: Long): JoinChannel =
        joinChannelRepository.findById(joinChannelId).orElseThrow{
            RuntimeException("Request not found")
        }.let {
            when(it.isActive){
                true -> it
                else -> throw RuntimeException("join channel not found")
            }
        }

    fun findByUserAndChannel(currentUser : User, channelId: Long) =
        joinChannelRepository.findByUserAndChannel(currentUser,channelService
            .findByChannelId(channelId)).let {
                return@let it
        }

    fun approvedRequest(joinChannelId:Long): JoinChannel =
        findById(joinChannelId).apply {
            requestStatus = RequestStatus.APPROVED
        }.let {
            joinChannelRepository.save(it)
        }

    fun rejectRequest(joinChannelId: Long): JoinChannel =
        findById(joinChannelId).apply {
            requestStatus = RequestStatus.APPROVED
        }.let {
            joinChannelRepository.save(it)
        }

    fun removeUserFromChannel(joinChannel:JoinChannel): JoinChannel =
        joinChannel.apply {
            requestStatus = RequestStatus.PENDING
            isActive = false
        }.let {
            joinChannelRepository.save(it)
        }
}
