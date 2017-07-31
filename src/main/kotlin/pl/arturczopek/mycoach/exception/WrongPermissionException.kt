package pl.arturczopek.mycoach.exception

/**
 * @Author Artur Czopek
 * @Date 26-05-2017
 */
class WrongPermissionException(message: String) : Exception(message) {
    companion object {
        private val serialVersionUID = -4438152382871196501L
    }
}
