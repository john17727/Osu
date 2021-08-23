package com.example.osu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.osu.ui.navigation.BottomNavItem
import com.example.osu.ui.navigation.BottomNavigationBar
import com.example.osu.ui.navigation.Navigation
import com.example.osu.ui.theme.OsuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OsuTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Dashboard",
                                    route = "dashboard",
                                    icon = Icons.Rounded.Dashboard
                                ),
                                BottomNavItem(
                                    name = "Workout",
                                    route = "workout",
                                    icon = Icons.Rounded.PlayArrow
                                ),
                                BottomNavItem(
                                    name = "Exercises",
                                    route = "exercises",
                                    icon = Icons.Rounded.FitnessCenter
                                ),
                                BottomNavItem(
                                    name = "Profile",
                                    route = "profile",
                                    icon = Icons.Rounded.AccountCircle
                                )
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OsuTheme {
        Greeting("Android")
    }
}