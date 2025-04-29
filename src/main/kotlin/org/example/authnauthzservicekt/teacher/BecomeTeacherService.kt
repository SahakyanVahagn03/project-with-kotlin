package org.example.authnauthzservicekt.teacher

import org.example.authnauthzservicekt.auth.UserRepository
import org.example.authnauthzservicekt.auth.UserService
import org.example.authnauthzservicekt.model.BecomeTeacherRequest
import org.example.authnauthzservicekt.model.RequestStatus
import org.example.authnauthzservicekt.model.Role
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class BecomeTeacherService(
    private val requestRepo: BecomeTeacherRequestRepository,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val restTemplateBuilder: RestTemplateBuilder
){
    fun becomeTeacherRequest(cv: String?): BecomeTeacherRequest {
//        val existingRequest = requestRepo?.findByUserId(userService.getCurrentUser().id)
        return  createRequest(cv)
    }

    private fun createRequest(cv: String?) : BecomeTeacherRequest =
        requestRepo.save(
            BecomeTeacherRequest(user =  userService.getCurrentUser()  ?: throw RuntimeException("current user not found"), cv = cv)
        )

    fun approveRequest(becomeTeacherRequest: Long) {
        findById(becomeTeacherRequest)?.apply {
            status = RequestStatus.APPROVED
            user.role = Role.TEACHER
        }?.also {
            requestRepo.save(it)
            userRepository.save(it.user)
        } ?: throw IllegalArgumentException("Request not found with id: $becomeTeacherRequest")
    }

    fun findById(becomeTeacherRequest: Long): BecomeTeacherRequest = requestRepo.findById(becomeTeacherRequest)
        .orElseThrow {
            RuntimeException("Request not found")
        }?.let {
            return it
        } ?: throw IllegalArgumentException("Request not found with id: $becomeTeacherRequest")

    fun findByUserId(userId: Long): BecomeTeacherRequest = requestRepo.findByUserId(userId)

    fun rejectRequest(becomeTeacherRequest: Long) {
        findById(becomeTeacherRequest)?.apply {
            status = RequestStatus.REJECTED
        }?.also {
            requestRepo.save(it)
        } ?: throw IllegalStateException("Request with id $becomeTeacherRequest could not be found")
    }

}