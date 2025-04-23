package org.example.authnauthzservicekt.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class TwilioConfig {
    @Value("\${twilio.account.sid}")
    lateinit var sid: String

    @Value("\${twilio.auth.token}")
    lateinit var token: String

    @Value("\${twilio.trial.fromNumber}")
    lateinit var fromNumber: String
}