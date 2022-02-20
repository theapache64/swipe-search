package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.theapache64.swipesearch.ui.composable.twyper.TwyperController


@Composable
fun Controllers(
    twyperController: TwyperController,
    modifier : Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = modifier
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
