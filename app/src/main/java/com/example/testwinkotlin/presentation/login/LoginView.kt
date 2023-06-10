package com.example.testwinkotlin.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testwinkotlin.R
import com.example.testwinkotlin.presentation.components.*

@Composable
fun LoginView(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToAppView: () -> Unit,
    navigateToLaunchScreenView: () -> Unit
) {

    val state = viewModel.state
    var mainActivity = LocalContext.current as FragmentActivity
    val isKeyboardOpen by keyboardAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .zIndex(0.0f)
            .imePadding()
    ) {

        when (state) {

            LoginViewState.error -> {
                ErrorView(
                    errorMessage = "Error: ${viewModel.errorMessage}",
                    okAction = {
                        viewModel.setEvent(LoginViewEvent.showLoginForm)
                    }
                )
            }

            LoginViewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    strokeWidth = Dp(value = 4F)
                )
            }

            LoginViewState.navigateToAppView -> {

                LaunchedEffect(viewModel.state) {
                    navigateToAppView()
                }
            }

            LoginViewState.navigateToLaunchScreenView -> {

                LaunchedEffect(viewModel.state) {
                    navigateToLaunchScreenView()
                }
            }

            LoginViewState.loginForm -> {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.height(150.dp))
                AppTextField(
                    value = viewModel.username,
                    placeholder = "Username",
                    onValueChange = { text ->
                        viewModel.username = text
                    }
                )
                AppTextField(
                    value = viewModel.password,
                    placeholder = "Password",
                    isPasswordField = true,
                    isError = viewModel.state.equals(LoginViewState.loginError),
                    onValueChange = { text ->
                        viewModel.password = text
                    }
                )
                AppButton(
                    text = "Login",
                    modifier = Modifier
                        .clickable {
                            viewModel.setEvent(LoginViewEvent.doLogin)
                        }
                )
            }
        }
    }
}