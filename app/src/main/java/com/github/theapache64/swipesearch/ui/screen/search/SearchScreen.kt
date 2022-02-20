package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.theapache64.swipesearch.R
import com.github.theapache64.swipesearch.ui.composable.twyper.rememberTwyperController
import com.github.theapache64.swipesearch.ui.theme.SwipeSearchTheme

@Preview()
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
            .padding(10.dp),
    ) {
        // Some top margin
        Spacer(modifier = Modifier.height(10.dp))

        // App title
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.height(10.dp))

        // The input field
        SearchInput(
            query = viewModel.uiState.query,
            onQueryChanged = { newQuery ->
                viewModel.onQueryChanged(newQuery)
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        viewModel.uiState.subTitle?.let { parallelMsg ->
            Text(
                text = parallelMsg,
                modifier = Modifier.align(CenterHorizontally),
                color = Color.White.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        val twyperController = rememberTwyperController()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val loadingMessage = viewModel.uiState.loadingMsg
            val errorMessage = viewModel.uiState.blockingMsg
            when {
                loadingMessage != null -> {
                    Loading(loadingMessage)
                }
                errorMessage != null -> {
                    Text(text = errorMessage)
                }
                else -> {
                    Cards(
                        items = viewModel.uiState.items,
                        onItemSwipedOut = viewModel::onItemSwipedOut,
                        onEndReached = {
                            viewModel.onPageEndReached()
                        },
                        modifier = Modifier.padding(10.dp),
                        twyperController = twyperController
                    )
                }
            }
        }

        if (viewModel.uiState.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(50.dp))
            Controllers(
                twyperController = twyperController,
                modifier = Modifier.align(CenterHorizontally)
            )
        }

    }
}

