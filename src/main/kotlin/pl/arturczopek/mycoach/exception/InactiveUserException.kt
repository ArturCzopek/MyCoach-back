package pl.arturczopek.mycoach.exception

/**
 * @Author Artur Czopek
 * @Date 30-06-2017
 */
class InactiveUserException(message: String) : Exception(message) {
    companion object {
        private val serialVersionUID = -4438152382871276501L
    }
}
