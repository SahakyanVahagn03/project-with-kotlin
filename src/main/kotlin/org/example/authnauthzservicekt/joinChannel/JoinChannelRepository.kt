package org.example.authnauthzservicekt.joinChannel

import org.example.authnauthzservicekt.model.Channel
import org.example.authnauthzservicekt.model.JoinChannel
import org.example.authnauthzservicekt.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JoinChannelRepository :  JpaRepository<JoinChannel, Long> {
    fun findByUserAndChannel(user: User, channel: Channel): JoinChannel?
}