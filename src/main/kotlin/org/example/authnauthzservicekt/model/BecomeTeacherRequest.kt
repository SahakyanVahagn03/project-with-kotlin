package org.example.authnauthzservicekt.model

import jakarta.persistence.*

@Entity
@Table(name = "become_teacher_request")
data class BecomeTeacherRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    var status: RequestStatus = RequestStatus.PENDING,

    var isActive: Boolean = false,

    var cv: String? = null
)