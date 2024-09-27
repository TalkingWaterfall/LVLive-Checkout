package com.lvlive.lvlivecheckout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AppMenu(navController: NavHostController) {
    Column {
        Button(onClick = { navController.navigate("productCreationScreen") }) {
            Text("Product Creation")
        }
        // Add more menu items as needed
    }
}