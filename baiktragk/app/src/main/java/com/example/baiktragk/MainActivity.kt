package com.example.baiktragk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import com.example.baiktragk.ui.theme.BaiktragkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            BaiktragkTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {

                    composable("home") {
                        HomeScreen(navController)
                    }

                    composable("invoice/{lt}/{th}") { backStackEntry ->

                        val lt =
                            backStackEntry.arguments?.getString("lt")?.toInt() ?: 0

                        val th =
                            backStackEntry.arguments?.getString("th")?.toInt() ?: 0

                        InvoiceScreen(navController, lt, th)
                    }
                }
            }
        }
    }
}