package pl.arturczopek.mycoach.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import pl.arturczopek.mycoach.repository.UserRepository
import pl.arturczopek.mycoach.repository.UserSettingRepository

/**
 * @Author Artur Czopek
 * @Date 30-06-2017
 */

@Service
class AppDataService(
        private val userRepository: UserRepository,
        private val userSettingRepository: UserSettingRepository,
        private val mailSender: MailSender
) {

    @Value("\${my-coach.version}")
    lateinit var version: String

    @Value("\${my-coach.mail}")
    lateinit var mail: String

    @Value("\${my-coach.mail-footer}")
    lateinit var mailFooter: String

    fun getAppData() = mapOf(
            "version" to version,
            "RAM" to getMemory(),
            "users" to userRepository.count()
    )

    fun sendEmail(title: String, content: String) {
        val emails = userSettingRepository.findAllEmails()
        val message = SimpleMailMessage().apply {
            from = mail
            subject = title
            text = getEmailText(content)
        }

        emails.forEach {
            message.to = arrayOf(it)
            mailSender.send(message)
        }
    }

    private fun getMemory() = "${Math.round(Runtime.getRuntime().totalMemory() / 10e6)} Mb"

    // TODO: thymeleaf
    private fun getEmailText(content: String) =
            //            """
//            |<html>
//            |<head>
//            |</head>
//            |<body>
//            |   $content
//            |   <br><br>
//            |   $mailFooter
//            |</body>
//            |</html>""".trimMargin()
            """
            |$content
            |
            |
            |$mailFooter""".trimMargin()
}
