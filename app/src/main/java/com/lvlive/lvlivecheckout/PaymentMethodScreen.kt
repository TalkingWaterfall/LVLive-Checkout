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
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment



@Composable
fun PaymentMethodScreen(navController: NavHostController, basket: List<Product>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Placeholder for payment options, can be replaced with actual content later
        LazyColumn(
            modifier = Modifier.weight(1f) // Take available vertical space
        ) {
            items(1) { // Placeholder for payment options, replace with actual options later
                Text("Payment option will be available here.")
            }
        }

        // Button to go back
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Add vertical padding
        ) {
            Text("Go Back")
        }

        // Add the BasketSummary at the bottom
        BasketSummary(basket)
    }
}
