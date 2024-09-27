package com.lvlive.lvlivecheckout

data class Product(
    val id: Int = 0,           // Default ID can be 0 but should not be used for new products
    val name: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0      // Quantity should be initialized to 0
) {
    companion object {
        fun create(name: String, price: Double, nextProductId: Int): Product {
            // Ensure a product is created with a valid ID
            return Product(id = nextProductId, name = name, price = price, quantity = 1) // Start quantity at 1
        }
    }
}
