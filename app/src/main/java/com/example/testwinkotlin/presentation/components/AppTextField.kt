package com.example.testwinkotlin.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AppTextField(
    isPasswordField: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    placeholder: String,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black)
) {

    //var text by remember { mutableStateOf(value) }
    var passwordVisible by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(Color.Gray) }

    if (isError) {
        borderColor = Color.Red
    }

    val colors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        textColor = Color.Black,
        errorLabelColor = Color.Red,
    )
    var font = MaterialTheme.typography.bodySmall
    var fontSize = font.fontSize
    var fontFamily = font.fontFamily
    //var borderColor = Color.Gray

    Row {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = fontSize,
                fontFamily = fontFamily
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(color = borderColor, width = 1.dp, shape = RoundedCornerShape(8.dp))
                .indicatorLine(
                    enabled = false,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = colors,
                    focusedIndicatorLineThickness = 0.dp,  //to hide the indicator line
                    unfocusedIndicatorLineThickness = 0.dp //to hide the indicator line
                )
                .onFocusChanged {
                    if (it.isFocused) {
                        borderColor = Color.Black
                    } else {
                        borderColor = Color.LightGray
                    }
                }
                .height(38.dp),
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,

            ) {
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = it,
                singleLine = singleLine,
                enabled = enabled,
                visualTransformation = VisualTransformation.None,
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Send
                    else Icons.Filled.Clear

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    if (isPasswordField) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)

                        }
                    }
                },
                placeholder = {
                    Text(
                        placeholder,
                        style = font,
                        modifier = Modifier
                    )
                },
                interactionSource = interactionSource,
                // keep horizontal paddings but change the vertical
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    top = 0.dp, bottom = 0.dp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    textColor = Color.Black,
                    errorLabelColor = Color.Red,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        /*TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Password",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            },
            //shape = RoundedCornerShape(38.dp),
            modifier = modifier
                .border(color = Color.Green, width = 1.dp, shape = RoundedCornerShape(8.dp))
                .height(45.dp)
                .indicatorLine(
                    enabled = enabled,
                    isError = false,
                    interactionSource = interactionSource,
                    focusedIndicatorLineThickness = 0.dp,  //to hide the indicator line
                    unfocusedIndicatorLineThickness = 0.dp,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor =  Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                .fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            ),
            trailingIcon = {
                if (isPasswordField) {
                    Icon(Icons.Filled.Add, "", tint = Blue)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Black,
                errorLabelColor = Color.Red,
            )
        )*/
    }
}