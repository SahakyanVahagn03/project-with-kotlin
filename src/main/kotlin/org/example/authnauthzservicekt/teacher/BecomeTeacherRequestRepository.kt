package org.example.authnauthzservicekt.teacher

import org.example.authnauthzservicekt.model.BecomeTeacherRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BecomeTeacherRequestRepository : JpaRepository<BecomeTeacherRequest, Long> {
    fun findByUserId(userId: Long): BecomeTeacherRequest
}