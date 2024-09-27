package com.lvlive.lvlivecheckout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun BasketScreen(basket: MutableList<Product>, navController: NavHostController) { // Change to MutableList and add NavHostController
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth() // Ensure it occupies vertical space
        ) {
            items(basket) { product ->
                Text("${product.name} - $${product.price} x ${product.quantity}")
            }
        }

        BasketSummary(basket) // Ensure the summary appears at the bottom

        // Clear Basket Button
        Button(
            onClick = {
                basket.clear() // Clear all items from the basket
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Add padding around the button
        ) {
            Text("Clear Basket")
        }

        // Proceed to Payment Button
        Button(
            onClick = { navController.navigate("paymentMethodScreen") }, // Navigate to payment method screen
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Add padding around the button
        ) {
            Text("Proceed to Payment")
        }
    }
}
