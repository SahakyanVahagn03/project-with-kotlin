package org.example.authnauthzservicekt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling


@ComponentScan("org.example.authnauthzservicekt.*")
@EntityScan("org.example.authnauthzservicekt.*")
@EnableScheduling
@SpringBootApplication
class AuthNAuthZServiceKtApplication

fun main(args: Array<String>) {
    runApplication<AuthNAuthZServiceKtApplication>(*args)
}
