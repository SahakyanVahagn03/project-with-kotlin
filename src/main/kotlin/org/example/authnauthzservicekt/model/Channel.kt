package org.example.authnauthzservicekt.model

import jakarta.persistence.*

@Entity
@Table(name = "channel")
data class Channel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val description: String,

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    var teacher: User,

    var isActive: Boolean = true
)