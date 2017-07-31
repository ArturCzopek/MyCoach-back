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
        val userRepository: UserRepository,
        val userSettingRepository: UserSettingRepository,
        val mailSender: MailSender) {

    @Value("\${my-coach.version}")
    var version = ""

    @Value("\${my-coach.mail}")
    var mail = ""

    @Value("\${my-coach.mail-footer}")
    var mailFooter = ""

    fun getAppData() = mapOf(
            "version" to version,
            "RAM" to getMemory(),
            "users" to userRepository.count()
    )

    fun sendEmail(title: String, content: String) {
        val emails = userSettingRepository.findAllEmails()
        val message = SimpleMailMessage()
        message.from = mail
        message.subject = title
        message.text = getEmailText(content)

        emails.forEach {
            message.to = arrayOf(it)
            mailSender.send(message)
        }
    }

    private fun getMemory() = "${Math.round(Runtime.getRuntime().totalMemory() / 10e6)} Mb"

    private fun  getEmailText(content: String) =
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
