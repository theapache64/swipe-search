package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.theapache64.swipesearch.R
import com.github.theapache64.swipesearch.ui.theme.SwipeSearchTheme

@Preview
@Composable
fun SearchScreenPreview() {
    SwipeSearchTheme {
        SearchScreen()
    }
}

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        SearchInput(
            query = viewModel.uiState.query,
            onQueryChanged = { newQuery ->
                viewModel.onQueryChanged(newQuery)
            }
        )
        Cards()
        Controllers()
    }
}

@Composable
fun Controllers() {
    // TODO("Not yet implemented")
}

@Composable
fun Cards() {
    // TODO("Not yet implemented")
}

@Composable
fun SearchInput(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.hint_search)) }
    )
}
