package org.example.authnauthzservicekt.channel

import org.example.authnauthzservicekt.model.Channel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChannelRepository :  JpaRepository<Channel, Long> {
}