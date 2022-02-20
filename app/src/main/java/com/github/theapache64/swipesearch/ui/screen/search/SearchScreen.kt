package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.github.theapache64.swipesearch.R
import com.github.theapache64.swipesearch.data.remote.Item
import com.github.theapache64.swipesearch.ui.composable.twyper.SwipedOutDirection
import com.github.theapache64.swipesearch.ui.composable.twyper.Twyper
import com.github.theapache64.swipesearch.ui.composable.twyper.TwyperController
import com.github.theapache64.swipesearch.ui.composable.twyper.rememberTwyperController
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
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.app_name), fontSize = 25.sp)

        SearchInput(
            query = viewModel.uiState.query,
            onQueryChanged = { newQuery ->
                viewModel.onQueryChanged(newQuery)
            }
        )

        val twyperController = rememberTwyperController()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val loadingMessage = viewModel.uiState.loadingMessage
            val errorMessage = viewModel.uiState.errorMessage
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
            Controllers(twyperController)
        }
    }
}

@Composable
private fun Loading(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = message)
    }
}

@Composable
fun Controllers(twyperController: TwyperController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp),
    ) {

        IconButton(onClick = {
            twyperController.swipeLeft()
        }) {
            Text(text = "❌", fontSize = 30.sp)
        }

        IconButton(onClick = {
            twyperController.swipeRight()
        }) {
            Text(text = "❤️", fontSize = 30.sp)
        }
    }
}

@Composable
fun Cards(
    items: List<Item>,
    onItemSwipedOut: (Item, SwipedOutDirection) -> Unit,
    onEndReached: () -> Unit,
    twyperController: TwyperController,
    modifier: Modifier = Modifier
) {
    Twyper(
        items = items,
        twyperController = twyperController,
        onItemRemoved = onItemSwipedOut,
        onEmpty = onEndReached,
        modifier = modifier
    ) { item -> Card(item) }
}

@Composable
private fun Card(item: Item) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(color = rememberRandomColor())
                .fillMaxWidth()
                .padding(10.dp)
                .weight(1f)
        ) {
            // Repo name
            Text(
                text = item.name,
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            // Stars
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 18.sp)) {
                        append(text = "⭐️\n")
                    }
                    withStyle(style = SpanStyle(fontSize = 13.sp)) {
                        append(text = "${item.stargazersCount}")
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = item.owner.avatarUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )

            Column {
                Text(text = item.owner.login, fontSize = 18.sp)
                Text(
                    text = item.description ?: "---",
                    maxLines = 2,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

private val rgbRange = 0..255

@Composable
fun rememberRandomColor(): Color {
    return remember {
        Color(
            red = rgbRange.random(),
            green = rgbRange.random(),
            blue = rgbRange.random(),
        )
    }
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
        placeholder = { Text(stringResource(id = R.string.hint_search)) },
        singleLine = true,
        maxLines = 1,
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
        }
    )
}
