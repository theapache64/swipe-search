package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.theapache64.swipesearch.data.remote.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val items: List<Item> = listOf()
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {


    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onQueryChanged(newQuery: String) {
        uiState = uiState.copy(query = newQuery)
    }

}