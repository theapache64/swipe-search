package com.github.theapache64.swipesearch.ui.screen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Search : Screen("search")
}