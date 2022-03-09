package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.theapache64.swipesearch.ui.composable.twyper.flip.TwyperFlipController

@Composable
fun FlipControllers(
    twyperFlipController: TwyperFlipController,
    modifier : Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = modifier
    ) {

        IconButton(onClick = {
            twyperFlipController.swipeLeft()
        }) {
            Text(text = "❌", fontSize = 30.sp)
        }

        IconButton(onClick = {
            twyperFlipController.flip()
        }) {
            Text(text = "🔀", fontSize = 30.sp)
        }

        IconButton(onClick = {
            twyperFlipController.swipeRight()
        }) {
            Text(text = "❤️", fontSize = 30.sp)
        }
    }
}
