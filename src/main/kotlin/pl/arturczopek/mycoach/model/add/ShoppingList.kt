package pl.arturczopek.mycoach.model.add;

import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

data class ShoppingList(
        val place: String,
        val prices: List<NewPrice>,
        val shoppingDate: Date?
)

