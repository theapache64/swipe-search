package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.github.theapache64.swipesearch.data.remote.Item
import com.github.theapache64.swipesearch.ui.composable.rememberRandomColor
import com.github.theapache64.swipesearch.ui.composable.twyper.SwipedOutDirection
import com.github.theapache64.swipesearch.ui.composable.twyper.Twyper
import com.github.theapache64.swipesearch.ui.composable.twyper.TwyperController

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
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            rememberRandomColor(),
                            rememberRandomColor()
                        )
                    )
                )
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
