package com.lvlive.lvlivecheckout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun StartingScreen(products: MutableList<Product>, navController: NavHostController) {
    Scaffold { paddingValues ->
        Row(modifier = Modifier.padding(paddingValues)) {
            AppMenu(navController)
            ProductList(products) // Use the mutable list
        }
    }
}