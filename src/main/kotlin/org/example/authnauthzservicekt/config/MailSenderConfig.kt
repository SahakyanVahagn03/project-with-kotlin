package org.example.authnauthzservicekt.config

import org.example.authnauthzservicekt.property.MailSendingProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


@Configuration
class MailSenderConfig(
    private val mailSendingProperties: MailSendingProperties

) {
    @Bean
    fun javaMailSender(): JavaMailSender{
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailSendingProperties.host
        mailSender.port = mailSendingProperties.port
        mailSender.username = mailSendingProperties.username
        mailSender.password = mailSendingProperties.password
        configureJavaMailProperties(mailSender.javaMailProperties)
        return mailSender
    }

    private fun configureJavaMailProperties(properties: Properties) {
        properties["mail.transport.protocol"] = mailSendingProperties.protocol
        properties["mail.smtp.auth"] = mailSendingProperties.auth
        properties["mail.smtp.starttls.enable"] = mailSendingProperties.starttlsEnable
        properties["mail.debug"] = mailSendingProperties.debug
    }
}
