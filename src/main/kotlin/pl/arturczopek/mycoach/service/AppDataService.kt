package pl.arturczopek.mycoach.service

import org.springframework.stereotype.Service

/**
 * @Author Artur Czopek
 * @Date 30-06-2017
 */

@Service
class AppDataService {

    fun getAppData() = mapOf(
            "version" to 1.0,
            "RAM" to 512,
            "users" to 16
    )
}
