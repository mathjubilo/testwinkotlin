package com.example.winkotlin.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.testwinkotlin.R

@Composable
fun TopBar(
    titleText: MutableState<String>,
    navigateUp: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            //.background(Color.Blue)
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
    ) {

        val (backButton, title) = createRefs()

        Image(
            painterResource(id = R.drawable.ic_chev_right_arrow_lux),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                //.background(Color.Yellow)
                .rotate(180.0f)
                .padding(horizontal = 15.dp)
                .width(18.dp)
                .constrainAs(backButton){
                    //alig(parent)
                    centerVerticallyTo(parent)
                }
                .clickable {
                    println("clicando")
                    navigateUp()
                }
        )


        Text(
            titleText.value
                .uppercase()
                .replace("\n", " "),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                //.background(Color.Red)
                .constrainAs(title){
                    centerTo(parent)
                }
        )
    }

    /*TopAppBar(
        title = {

            Text(
                text = titleText.value,
                fontSize = 18.sp,
                modifier = Modifier
                    .background(Color.Yellow)
                    .fillMaxWidth()
            )
                },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.Black,
        modifier = Modifier
            .padding(0.dp),
        actions = {
            Text("Hello")
        }
        navigationIcon = {
            IconButton(
                onClick = {
                /*TODO*/
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Ir hacia arriba",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    )*/
}