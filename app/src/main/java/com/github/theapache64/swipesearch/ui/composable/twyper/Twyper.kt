package com.github.theapache64.swipesearch.ui.composable.twyper

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SwipedOutDirection {
    LEFT, RIGHT
}

@Composable
inline fun <reified T> Twyper(
    items: List<T>,
    onItemRemoved: (T, SwipedOutDirection) -> Unit,
    onEndReached: () -> Unit = {},
    twyperController: TwyperController = rememberTwyperController(),
    modifier: Modifier = Modifier,
    crossinline renderItem: @Composable (T) -> Unit
) {
    if (items.isEmpty()) {
        onEndReached()
    }
    Box(modifier = modifier) {
        val list = items.take(2).reversed()
        list.forEachIndexed { index, item ->
            key("$item$index") {
                val cardController = rememberCardController()
                if (index == list.lastIndex) {
                    twyperController.currentCardController = cardController
                }
                if (!cardController.isCardOut()) {
                    Card(
                        modifier = Modifier
                            .padding(top = (index * 2).dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        println("Drag ended")
                                        cardController.onDragEnd()
                                    },
                                    onDragCancel = {
                                        println("Drag cancelled")
                                        cardController.onDragCancel()
                                    },
                                    onDrag = { change, dragAmount ->
                                        println("Being dragged")
                                        change.consumePositionChange()
                                        cardController.onDrag(dragAmount)
                                    }
                                )
                            }
                            .graphicsLayer(
                                translationX = cardController.cardX,
                                translationY = cardController.cardY,
                                rotationZ = cardController.rotationFraction
                            )
                    ) {
                        renderItem(item)
                    }
                } else {
                    val outDirection = cardController.getSwipedOutDirection()
                    onItemRemoved(item, outDirection)
                }
            }
        }
    }
}

@Preview
@Composable
fun TwyperPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val items = remember { mutableStateListOf(*('A'..'Z').toList().toTypedArray()) }
        val twyperController = rememberTwyperController()
        Twyper(
            items = items,
            twyperController = twyperController,
            onItemRemoved = { item, direction ->
                println("Item removed: $item -> $direction")
                items.remove(item)
            },
            onEndReached = {
                println("End reached")
            }
        ) { item ->
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$item", fontSize = 60.sp)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

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
                Text(text = "✅", fontSize = 30.sp)
            }
        }
    }
}