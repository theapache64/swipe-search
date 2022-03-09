package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.github.theapache64.swipesearch.ui.composable.twyper.SwipedOutDirection
import com.github.theapache64.swipesearch.ui.composable.twyper.flip.TwyperFlip
import com.github.theapache64.swipesearch.ui.composable.twyper.flip.TwyperFlipController


@Composable
fun FlipCards(
    items: List<Item>,
    onItemSwipedOut: (Item, SwipedOutDirection) -> Unit,
    onEndReached: () -> Unit,
    twyperFlipController: TwyperFlipController,
    boxModifier: () -> Modifier
) {
    TwyperFlip(
        items = items,
        twyperFlipController = twyperFlipController,
        onItemRemoved = onItemSwipedOut,
        onEmpty = onEndReached,
        cardModifier = boxModifier,
        front = { item -> Card(item) },
        back = {item -> ReverseCard(item) }
    )
}

@Composable
fun ReverseCard(item: Item) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Description",
                style = MaterialTheme.typography.h4
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = item.description ?: "No Description Available",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun Card(item: Item) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                    .padding(10.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(10.dp)
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
