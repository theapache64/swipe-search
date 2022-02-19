package com.github.theapache64.swipesearch.ui.screen.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.theapache64.swipesearch.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    var greetingsRes by mutableStateOf(R.string.label_hello_world)
        private set

    fun onClickMeClicked() {
        // Toggling message
        greetingsRes = if (greetingsRes == R.string.label_hello_world) {
            R.string.label_hello_compose
        } else {
            R.string.label_hello_world
        }
    }
}