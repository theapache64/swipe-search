package com.github.theapache64.swipesearch.ui.composable.twyper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
fun rememberTwyperController(): TwyperController {
    return remember { TwyperControllerImpl() }
}

interface TwyperController {
    var currentCardController: CardController?
    fun swipeRight()
    fun swipeLeft()
}

class TwyperControllerImpl : TwyperController {
    override var currentCardController: CardController? = null

    override fun swipeRight() {
        currentCardController?.swipeRight()
    }

    override fun swipeLeft() {
        currentCardController?.swipeLeft()
    }
}