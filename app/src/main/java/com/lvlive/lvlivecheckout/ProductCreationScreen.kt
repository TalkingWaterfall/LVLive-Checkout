package com.lvlive.lvlivecheckout

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import android.util.Log

@Composable
fun ProductCreationScreen(
    navController: NavHostController,
    nextProductId: Int, // Add this parameter
    onProductCreated: (Product) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    val context = LocalContext.current // For Toast messages

    Column {
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") }
        )
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") }
        )
        Button(onClick = {
            // Convert the product price to Double, handle invalid input gracefully
            val price = productPrice.toDoubleOrNull()
            if (price != null && productName.isNotBlank()) {
                // Create the product and pass it back
                val product = Product.create(
                    name = productName,
                    price = price,
                    nextProductId = nextProductId // Pass nextProductId to create
                )

                // Log the created product
                Log.d("ProductCreation", "Creating product: $product")

                onProductCreated(product) // Call the onProductCreated function
                navController.navigate("startingScreen") // Navigate back after creation
            } else {
                // Show a Toast message for invalid input
                Toast.makeText(context, "Invalid product name or price.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Create Product")
        }
    }
}
