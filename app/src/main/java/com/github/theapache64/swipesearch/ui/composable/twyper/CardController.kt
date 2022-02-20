package com.github.theapache64.swipesearch.ui.composable.twyper

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * To control individual card
 */
interface CardController {
    val cardX: Float
    val cardY: Float
    val rotation: Float

    fun onDrag(dragAmount: Offset)
    fun onDragCancel()
    fun onDragEnd()
    fun isCardOut(): Boolean
    fun swipeRight()
    fun swipeLeft()
    fun getSwipedOutDirection(): SwipedOutDirection
}


@Composable
fun rememberCardController(): CardController {
    val scope = rememberCoroutineScope()
    val screenWidth =
        with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    return remember {
        val swipeX = Animatable(0f)
        val swipeY = Animatable(0f)
        CardControllerImpl(swipeX, swipeY, scope, screenWidth)
    }
}


class CardControllerImpl(
    private val swipeX: Animatable<Float, AnimationVector1D>,
    private val swipeY: Animatable<Float, AnimationVector1D>,
    private val scope: CoroutineScope,
    private val screenWidth: Float,
) : CardController {

    override val cardX: Float
        get() = swipeX.value

    override val cardY: Float
        get() = swipeY.value

    override val rotation: Float
        get() = (swipeX.value / 60).coerceIn(-40f, 40f)

    override fun onDrag(dragAmount: Offset) {
        scope.apply {
            launch { swipeX.animateTo(swipeX.targetValue + dragAmount.x) }
            launch { swipeY.animateTo(swipeY.targetValue + dragAmount.y) }
        }
    }

    override fun onDragCancel() {
        scope.apply {
            launch { swipeX.animateTo(0f) }
            launch { swipeY.animateTo(0f) }
        }
    }

    override fun onDragEnd() {
        val isSwipedOneThird = abs(swipeX.targetValue) > abs(screenWidth) / 3
        if (isSwipedOneThird) {
            scope.launch {
                if (swipeX.targetValue > 0) {
                    swipeX.animateTo(screenWidth, tween(400))
                } else {
                    swipeX.animateTo(-screenWidth, tween(400))
                }
            }
        } else {
            // go back to origin
            onDragCancel()
        }
    }

    override fun isCardOut(): Boolean {
        return abs(swipeX.value) == screenWidth
    }

    override fun swipeRight() {
        scope.launch {
            swipeX.animateTo(screenWidth, tween(400))
        }
    }

    override fun swipeLeft() {
        scope.launch {
            swipeX.animateTo(-screenWidth, tween(400))
        }
    }

    override fun getSwipedOutDirection(): SwipedOutDirection {
        return if (swipeX.targetValue < 0) {
            SwipedOutDirection.LEFT
        } else {
            SwipedOutDirection.RIGHT
        }
    }

}