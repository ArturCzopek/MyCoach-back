package pl.arturczopek.mycoach.utils.http

/**
 * @Author arturczopek
 * @Date 07-05-2017
 */

data class ErrorMessage(
        val message: String,
        val url: String,
        val exception: String
)
