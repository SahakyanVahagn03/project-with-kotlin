package org.example.authnauthzservicekt.teacher

import org.example.authnauthzservicekt.model.BecomeTeacherRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class TeacherController (
    val becomeTeacherService: BecomeTeacherService
) {

    @PostMapping("/become-teacher")
    fun becomeTeacherRequest(
        @RequestBody cv: String
    ): BecomeTeacherRequest {
        return becomeTeacherService.becomeTeacherRequest(cv)
    }

    @GetMapping("/approved/teacher/{becomeTeacherId}")
    fun approvedTeacherRequest(
        @PathVariable becomeTeacherId: Long,
    ) {
        becomeTeacherService.approveRequest(becomeTeacherId)
    }

    @GetMapping("/reject-user/{becomeTeacherId}")
    fun becomeTeacherRequest(
        @PathVariable becomeTeacherId: Long,
    ) {
        becomeTeacherService.rejectRequest(becomeTeacherId)
    }
}