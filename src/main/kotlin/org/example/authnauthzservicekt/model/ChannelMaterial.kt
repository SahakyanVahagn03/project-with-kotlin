package org.example.authnauthzservicekt.model

import jakarta.persistence.*

@Entity
@Table(name = "channel_material")
data class ChannelMaterial(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    val type: MaterialType,

    val url: String,

    @ManyToOne
    @JoinColumn(name = "channel_id")
    val channel: Channel
)