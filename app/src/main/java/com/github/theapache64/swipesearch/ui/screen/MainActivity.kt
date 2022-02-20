package com.github.theapache64.swipesearch.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.theapache64.swipesearch.ui.screen.search.SearchScreen
import com.github.theapache64.swipesearch.ui.screen.splash.SplashScreen
import com.github.theapache64.swipesearch.ui.theme.SwipeSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SwipeSearchTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {

                    // Splash
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onSplashFinished = {
                                val options = NavOptions.Builder()
                                    .setPopUpTo(Screen.Splash.route, inclusive = true)
                                    .build()
                                navController.navigate(
                                    Screen.Search.route,
                                    options
                                ) // Move to dashboard
                            }
                        )
                    }

                    // Dashboard
                    composable(Screen.Search.route) {
                        SearchScreen()
                    }
                }
            }
        }
    }
}
