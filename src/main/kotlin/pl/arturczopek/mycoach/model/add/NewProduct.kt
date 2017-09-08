package pl.arturczopek.mycoach.model.add;

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

data class NewProduct(
        val productId: Long,
        val productName: String,
        val image: Array<Byte>?
)
