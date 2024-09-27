@file:OptIn(ExperimentalFoundationApi::class)

package com.lvlive.lvlivecheckout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.lvlive.lvlivecheckout.ui.theme.LVLiveCheckoutTheme
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {
    var nextProductId = 1 // Define nextProductId here
    private val REQUEST_CODE_LOCATION = 1 // Define your request code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            // REQUEST_CODE_LOCATION should be defined on your app level
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION)
        }

        setContent {
            LVLiveCheckoutTheme {
                val database = FirebaseDatabase.getInstance()
                Log.d("FirebaseDatabase", "Database initialized successfully")

                val navController = rememberNavController()
                val products = remember { mutableStateListOf<Product>() }
                val basket = remember { mutableStateListOf<Product>() }
                val productsRef = database.reference.child("products")

                // Fetch products and the highest product ID from Firebase
                LaunchedEffect(Unit) {
                    productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val productList = mutableListOf<Product>()
                            var highestId = 0

                            for (productSnapshot in dataSnapshot.children) {
                                val product = productSnapshot.getValue(Product::class.java)
                                if (product != null) {
                                    productList.add(product)
                                    if (product.id > highestId) {
                                        highestId = product.id // Find the highest product ID
                                    }
                                }
                            }

                            products.clear()
                            products.addAll(productList)
                            nextProductId = highestId + 1 // Set nextProductId to highestId + 1
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseDatabase", "Failed to read value.", error.toException())
                        }
                    })
                }

                NavHost(navController = navController, startDestination = "startingScreen") {
                    composable("startingScreen") {
                        StartingScreen(navController, products, basket, nextProductId) // Pass nextProductId
                    }
                    composable("productCreationScreen/{nextProductId}") { backStackEntry ->
                        val nextProductId = backStackEntry.arguments?.getString("nextProductId")?.toInt() ?: 1
                        ProductCreationScreen(navController, nextProductId) { newProduct ->
                            // Check if the product ID already exists in the local products list
                            if (products.none { it.id == newProduct.id }) {
                                // Add the product to the database and the local list
                                productsRef.child(newProduct.id.toString()).setValue(newProduct).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("ProductCreation", "Product added to database successfully: ${newProduct.id}")
                                        products.add(newProduct)
                                        this@MainActivity.nextProductId++
                                    } else {
                                        Log.e("ProductCreation", "Failed to add product: ${task.exception?.message}")
                                    }
                                }
                            } else {
                                Log.e("ProductCreation", "Product with ID ${newProduct.id} already exists.")
                            }
                        }
                    }
                    composable("basketScreen") {
                        BasketScreen(basket, navController) // Pass navController to BasketScreen
                    }
                    composable("paymentMethodScreen") {
                        PaymentMethodScreen(navController, basket) // Add PaymentMethodScreen to NavHost
                    }
                }
            }
        }
    }
}

@Composable
fun StartingScreen(
    navController: NavHostController,
    products: List<Product>,
    basket: MutableList<Product>, // Keep this as a mutable list
    nextProductId: Int // Add this parameter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f) // Allow the list to take available vertical space
        ) {
            items(products) { product ->
                Text(
                    text = "${product.name} - $${product.price}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Directly modify the basket state
                            val existingProduct = basket.find { it.id == product.id }
                            if (existingProduct != null) {
                                existingProduct.quantity++ // Increase quantity
                                // Notify that the basket has changed so the UI can update
                                basket.remove(existingProduct)
                                basket.add(existingProduct.copy())
                            } else {
                                basket.add(product.copy(quantity = 1)) // Add new product with quantity
                            }
                        }
                        .padding(8.dp) // Add padding inside the text area
                )
            }
        }

        Button(
            onClick = { navController.navigate("paymentMethodScreen") }, // Navigate to payment method screen
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Proceed to Payment")
        }
        // Button for product creation
        //Button(
        //    onClick = { navController.navigate("productCreationScreen/${nextProductId}") },
        //    modifier = Modifier
        //        .fillMaxWidth() // Ensure the button takes full width
        //        .padding(vertical = 8.dp) // Space between buttons
        //) {
        //    Text("Create Product")
        //}

        // Button to view basket
        Button(
            onClick = { navController.navigate("basketScreen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("View Basket")
        }

        // Call BasketSummary with the basket to display total items and value
        BasketSummary(basket)
    }
}

@Composable
fun BasketSummary(basket: List<Product>) {
    val totalItems = basket.sumOf { it.quantity }
    val totalValue = basket.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Total items: $totalItems")
        Text("Total value: $$totalValue")
    }
}