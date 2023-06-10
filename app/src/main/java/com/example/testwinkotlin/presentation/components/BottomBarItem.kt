package com.example.winkotlin.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun BottomBarItem(
    title: String,
    image: Int,
    screen: String,
    currentDestination: String?,
    //currentDestination: NavDestination?,
    navigate: (String)->Unit,
    isFilterButton: Boolean = screen.equals("filters")
){

    val selected = currentDestination == screen
    //val selected = currentDestination?.hierarchy?.any { it.route == screen } == true
    var imageColor =
        if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary
    var textColor = imageColor
    var topPadding: Dp = 10.dp
    if (isFilterButton) {
        imageColor = Color.White
        topPadding = 0.dp
    }

    Column(
        Modifier
            .fillMaxHeight()
            //.background(Color.Yellow)
            .padding(top = 5.dp)
            .clickable(onClick = {

                navigate(screen)
                /*navController.navigate(screen.screen_route) {

                    popUpTo(navController.graph.findStartDestination().id)
                }*/
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Box() {

            if (isFilterButton) {
                Box(Modifier
                    .size(35.dp)
                    .align(Alignment.Center)
                    .clip(
                        RoundedCornerShape(17.5.dp)
                    )
                    .background(Color.Black)
                )
            }

            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(imageColor),
                modifier = Modifier
                    .padding(top = topPadding)
                    .zIndex(1f)
                    .width(17.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = 8.sp,
            modifier =  Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 4.dp
                )
            //.background(Color.Yellow)
        )
    }
    //}
}