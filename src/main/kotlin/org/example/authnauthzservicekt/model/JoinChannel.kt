package org.example.authnauthzservicekt.model

import jakarta.persistence.*

@Entity
@Table(name = "join_channel")
data class JoinChannel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "channel_id")
    val channel: Channel,

    @Enumerated(EnumType.STRING)
    var requestStatus: RequestStatus = RequestStatus.PENDING,

    var isActive: Boolean = true
)