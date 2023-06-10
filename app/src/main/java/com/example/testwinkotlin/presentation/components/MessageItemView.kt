package com.inditex.wms.rfdevice.win.winkotlin.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.testwinkotlin.presentation.components.AppText
import com.example.testwinkotlin.*
import com.example.testwinkotlin.R

@Composable
fun MessageItemView(
    user: String = "BRIAN HORN",
    date: String = "23 DEC 20:12",
    message: String = "ATC Ramón Puentes nos confirma que\n" +
            "TGW no realizó ninguna acción, y que reinició mantenimiento.",
    isMine: Boolean = false,
    isInternal: Boolean = false,
    isDownload: Boolean = false,
    isShowAll: Boolean = false,
    noTriangles: Boolean = false
) {
    val isBigMessage = message.count() > 300
    val iconsTopPadding = 5.dp
    val paddingHorizontal = 16
    val paddingVertical = 8
    val strokeSize = 4f
    val triangleWidth = 7f
    val triangleHeight = 12f
    val triangleBottomPadding = 5.dp
    val containerGreyColor = MaterialTheme.colorScheme.onPrimary
    val containerBlueColor = MaterialTheme.colorScheme.scrim
    var triangleColor = containerBlueColor
    var arrowRotation: Float = 0f
    if (!isShowAll) {
        arrowRotation = 180f
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!isMine && !noTriangles) {
            Triangle(
                width = triangleWidth,
                height = triangleHeight,
                color = triangleColor,
                pointingRight = false,
                modifier = Modifier
                    .padding(bottom = triangleBottomPadding)
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(triangleWidth.dp)
            )
        }
        Box(
            modifier = Modifier
                .background(Color.Black)
                .height(IntrinsicSize.Min)
                .weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(containerBlueColor)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(
                            start = paddingHorizontal.dp,
                            end = paddingHorizontal.dp,
                            top = paddingVertical.dp
                        )
                        .fillMaxWidth()
                ) {
                    Column(

                    ) {
                        AppText(
                            text = user,
                            style = MaterialTheme.typography.displayMedium
                        )
                        AppText(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (isDownload) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.message_download_svg
                                ),
                                "",
                                modifier = Modifier
                                    .padding(top = iconsTopPadding)
                            )
                        }
                        Spacer(modifier = Modifier.width(22.dp))
                        if (isBigMessage) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.message_arrow_svg
                                ),
                                "",
                                modifier = Modifier
                                    .width(22.dp)
                                    .padding(top = iconsTopPadding)
                                    .rotate(arrowRotation)
                            )
                        }
                    }
                }
                AppText(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(
                            start = paddingHorizontal.dp,
                            end = paddingHorizontal.dp,
                            top = 10.dp,
                            bottom = paddingVertical.dp
                        )
                )
            }
            if (isInternal) {
                Spacer(
                    modifier = Modifier
                        .width(strokeSize.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outline)
                )
            }
        }
        if (isMine  && !noTriangles) {
            Triangle(
                width = triangleWidth,
                height = triangleHeight,
                color = triangleColor,
                pointingRight = true,
                modifier = Modifier
                    .padding(bottom = triangleBottomPadding)
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(triangleWidth.dp)
            )
        }
    }

}