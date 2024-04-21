package com.aria.danesh.sidemenu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.util.lerp

@Preview(showBackground = true)
@Composable
fun TestSideMenu() {

    val m = remember { mutableStateOf(MenuState.COLLAPSE) }
    Surface(Modifier.fillMaxSize()) {
        SideMenu(
            {
                Spacer(
                    it.background(Color.Cyan)
                )
            },
            sideMenuState = m.value,
            layoutDirection = LayoutDirection.Rtl,
            onSideMenuStateChange = { m.value = it }) {
            Spacer(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }
}


@Composable
fun SideMenu(
    menuContent: @Composable (modifier: Modifier) -> Unit,
    sideMenuState: MenuState,
    layoutDirection: LayoutDirection,
    onSideMenuStateChange: (MenuState) -> Unit,
    mainContent: @Composable () -> Unit
) {

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {


        val isDoneDragging = remember {
            mutableStateOf(true)
        }
        val stiffness = 260f
        val drawer: Float = 150f

        val coroutineScope = rememberCoroutineScope()

        val tranX = remember {
            Animatable(
                0f
            )
        }

        val transition = updateTransition(tranX.value, label = "ToolBar State")


        val mainRotation by transition.animateFloat(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->
            if (layoutDirection == LayoutDirection.Ltr)
                lerp(0f, 10f, x / drawer)
            else
                lerp(0f, -10f, x / drawer)

        }

        val mainScale by transition.animateFloat(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->
            lerp(1f, 0.5f, x / drawer)
        }
        val mainTranslation by transition.animateDp(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->
            if (layoutDirection == LayoutDirection.Ltr)
                lerp(0, 120, x / drawer).dp
            else
                lerp(0, -120, x / drawer).dp

        }

        val mainShadow by transition.animateDp(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->

            lerp(0, 1, x / drawer).dp

        }


        val menuScale by transition.animateFloat(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->

            lerp(0f, 1f, x / drawer)

//        when (state) {
//            MenuState.EXPENDED -> 1f
//            MenuState.COLLAPSE -> 0f
//        }

        }
        val menuTranslation by transition.animateDp(
            transitionSpec = {
                spring(
                    stiffness = stiffness,
                    dampingRatio = 0.46f,
                )
            }, label = "color"

        ) { x ->
            lerp((200 * -1), 0, x / drawer).dp


//        when (state) {
//            MenuState.EXPENDED -> 0.dp
//            MenuState.COLLAPSE -> (200.dp * -1)
//        }

        }
        SideEffect {
            if (isDoneDragging.value)
                if (sideMenuState == MenuState.EXPENDED)
                    coroutineScope.launch {
                        tranX.animateTo(10f * drawer)
                    }
                else
                    coroutineScope.launch {
                        tranX.animateTo(0f)
                    }
        }


        tranX.updateBounds(0f, drawer)

        val draggableState = if (layoutDirection == LayoutDirection.Ltr)
            rememberDraggableState(onDelta = {
                coroutineScope.launch {
                    tranX.snapTo(tranX.value + it)
                }

            }) else
            rememberDraggableState(onDelta = {
                coroutineScope.launch {
                    tranX.snapTo(tranX.value - it)
                }

            })
        val decay = rememberSplineBasedDecay<Float>()


        Surface(Modifier
            .draggable(draggableState, orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    isDoneDragging.value = true
                    if (layoutDirection == LayoutDirection.Ltr) {
                        val decayX = decay.calculateTargetValue(tranX.value, velocity)

                        coroutineScope.launch {
                            val targetX = if (decayX > drawer * 0.5) {
                                drawer
                            } else {
                                0f
                            }
                            val canReachTarget =
                                (decayX > targetX && targetX == drawer) || (decayX < targetX && targetX == 0f)

//                            if (canReachTarget)
//                                tranX.animateDecay(velocity, decay)
//                            else
//                                tranX.animateTo(targetX, initialVelocity = velocity)

                            onSideMenuStateChange(
                                if (targetX == drawer)
                                    MenuState.EXPENDED
                                else
                                    MenuState.COLLAPSE
                            )

                        }
                    }
                    else{
                        val decayX = decay.calculateTargetValue(tranX.value, velocity)

                        coroutineScope.launch {
                            val targetX = if (decayX > drawer * 0.5) {
                                drawer
                            } else {
                                0f
                            }
                            val canReachTarget =
                                (decayX > targetX && targetX == drawer) || (decayX < targetX && targetX == 0f)

//                            if (canReachTarget)
//                                tranX.animateDecay(velocity, decay)
//                            else
//                                tranX.animateTo(targetX, initialVelocity = velocity)

                            onSideMenuStateChange(
                                if (targetX != drawer)
                                    MenuState.EXPENDED
                                else
                                    MenuState.COLLAPSE
                            )

                        }
                    }
                },
                onDragStarted = {
                    isDoneDragging.value = false
                }
            )


            .fillMaxSize(), color = Color.Transparent) {

            Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = menuScale
                        translationX = if (layoutDirection == LayoutDirection.Ltr) menuTranslation.toPx() else -menuTranslation.toPx()
                    },
                Arrangement.Start,
            ) {
                menuContent(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                )
            }

            Surface(modifier =
            Modifier
                .fillMaxSize()
                .graphicsLayer {

                    scaleY = mainScale
                    scaleX = mainScale
                    rotationY = mainRotation
                    translationX = mainTranslation.toPx()

                }
                .background(
                    color = Color.Transparent
                )
                .shadow(
                    mainShadow,
                    shape = RoundedCornerShape(lerp(0, 15, tranX.value / drawer).dp),
                    clip = true
                )
                .clickable(enabled = sideMenuState == MenuState.EXPENDED) {
                    onSideMenuStateChange(MenuState.COLLAPSE)
                }) {
                mainContent()
            }

        }
    }
}

