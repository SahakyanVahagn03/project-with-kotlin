package org.example.authnauthzservicekt.property

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class MailSendingProperties(
    @Value("\${spring.mail.host}")
    val host: String,
    @Value("\${spring.mail.port}")
    val port: Int,
    @Value("\${spring.mail.username}")
    val username: String,
    @Value("\${spring.mail.password}")
    val password: String,
    @Value("\${spring.mail.properties.mail.transport.protocol}")
    val protocol: String,
    @Value("\${spring.mail.properties.mail.smtp.auth}")
    val auth: Boolean,
    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    val starttlsEnable: Boolean,
    @Value("\${spring.mail.properties.mail.debug}")
    val debug: Boolean
) {
}