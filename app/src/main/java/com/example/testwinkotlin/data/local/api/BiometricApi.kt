package com.example.testwinkotlin.data.local.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher

class BiometricsApi(
    val mainActivity: FragmentActivity? = null,
    val secretKeyName: String? = "",
    var stringToEncrypt: String? = "",
    var encryptedString: String? = "",
    val response: BiometricResponse? = null
) {
    var decryptedString = ""
    private var doEncryption: Boolean = false
    var state: MutableState<String> = mutableStateOf("")
    var keStoreApi: IKeyStoreApi = KeyStoreApi()

    var biometricPrompt: BiometricPrompt? = mainActivity?.let {
        createBiometricPrompt(
            mainActivity = it,
            context = mainActivity.applicationContext
        )
    }
    var promptInfo: BiometricPrompt.PromptInfo = createPromptInfo()

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun uiTest(){
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {

            TextField(
                value = state.value,
                onValueChange = { newText ->
                    state.value = newText.trimStart { it == '0' }
                }
            )
            Row() {
                Text(text = "Encrypt", modifier = Modifier
                    .clickable {

                        stringToEncrypt = state.value
                        authenticateToEncrypt()
                    })
                Text(text = "Decrypt", modifier = Modifier
                    .clickable {
                        decryptedString = state.value
                        authenticateToDecrypt()
                    })
            }

            Text(text = state.value)
        }
    }

    private fun createBiometricPrompt(mainActivity: FragmentActivity, context: Context): BiometricPrompt {

        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("Biometrics","$errorCode :: $errString")

                when(errorCode) {
                    BiometricPrompt.ERROR_CANCELED -> {
                        Log.d("BiometricsApi", "Biometric Auth error: ERROR_CANCELED")
                        response?.onBiometricError(errorCode.toString(), errString.toString())
                    }
                    BiometricPrompt.ERROR_LOCKOUT -> {
                        Log.d("BiometricsApi", "Biometric Auth error: ERROR_LOCKOUT")
                        response?.onBiometricError(errorCode.toString(), errString.toString())
                    }
                    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                        Log.d("BiometricsApi", "Biometric Auth error: ERROR_LOCKOUT_PERMANENT")
                        response?.onBiometricError(errorCode.toString(), errString.toString())
                    }
                    BiometricPrompt.ERROR_USER_CANCELED -> {
                        Log.d("BiometricsApi", "Biometric Auth error: ERROR_USER_CANCELED")
                        response?.onBiometricError(errorCode.toString(), errString.toString())
                    }
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                        Log.d("BiometricsApi", "Biometric Auth error: ERROR_NEGATIVE_BUTTON")
                        response?.onBiometricError(errorCode.toString(), errString.toString())
                    }
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("Biometrics","Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("Biometrics","Authentication was successful")

                response?.onBiometricSuccess(null)
            }
        }

        val biometricPrompt = BiometricPrompt(mainActivity, executor, callback)
        return biometricPrompt
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación Biométrica WIN")
            .setDescription("!identifíquese!")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .setConfirmationRequired(false)
            .build()
        return promptInfo
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun authenticateToEncrypt() {

        doEncryption = true


        if (mainActivity?.applicationContext?.let { BiometricManager.from(it).canAuthenticate() } == BiometricManager.BIOMETRIC_SUCCESS) {

            secretKeyName?.let {
                val cipher = keStoreApi.getInitializedCipherForEncryption(
                    keyName = it,
                    withAuthorization = true
                )
                biometricPrompt?.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }

        if (mainActivity?.applicationContext?.let { BiometricManager.from(it).canAuthenticate() } == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {

            response?.askUserForPermission()
        }

        val biometricManager = mainActivity?.applicationContext?.let { BiometricManager.from(it) }
        when (biometricManager?.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {

                Log.d("Biometrics", "App can authenticate using biometrics.")

                secretKeyName?.let {
                    val cipher = keStoreApi.getInitializedCipherForEncryption(
                        keyName = it,
                        withAuthorization = true
                    )
                    biometricPrompt?.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
                }
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Biometrics", "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("Biometrics", "Biometric features are currently unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                askUserForPermission()
            }
        }
    }

    fun authenticateToDecrypt() {
        doEncryption = false

        mainActivity?.applicationContext?.let{ context ->
            Log.d("Biometrics",
                "Status ${BiometricManager.from(context).canAuthenticate()}")

            if (BiometricManager.from(context)
                    .canAuthenticate() == BiometricManager
                    .BIOMETRIC_SUCCESS
            ) {

                encryptedString?.let { encString ->
                    val separatedIVFromStringToByteArray = keStoreApi.separateIVFromEncDataAndConvertToByteArray(encString)

                    secretKeyName?.let { secretKey ->
                        val cipher = keStoreApi.getInitializedCipherForDecryption(secretKey,
                            separatedIVFromStringToByteArray,
                            true)

                        biometricPrompt?.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
                    }
                }
            }

            if (BiometricManager.from(mainActivity.applicationContext).canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
            ) {
                //mainActivity.
                askUserForPermission()
            }
        }
    }

    fun askUserForPermission() {

        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
        }
    }

    private fun decrypt(encryptedString: String, cipher: Cipher) {
        secretKeyName?.let {
            val decryptedString = keStoreApi.decryptWithExternalCipher(
                cipher = cipher,
                data = encryptedString,
                forKey = it,
                withBiometricAuth = true
            )
            updateState(data = decryptedString)
        }
    }

    private fun encrypt(stringToEncrypt: String, cipher: Cipher) {
        secretKeyName?.let { secretKey ->
            val encryptedString = keStoreApi.encryptWithExternalCipher(
                cipher = cipher,
                data = stringToEncrypt,
                forKey = secretKey,
                withBiometricAuth = true
            )
            updateState(data = encryptedString)
        }
    }

    private fun updateState(data: String) {
        state.value = data
    }

    fun showDialog() {
        mainActivity?.applicationContext?.let { context ->
            if (BiometricManager.from(context)
                    .canAuthenticate() == BiometricManager
                    .BIOMETRIC_SUCCESS
            ) {
                biometricPrompt?.authenticate(promptInfo)
            }

            if (BiometricManager.from(mainActivity.applicationContext)
                    .canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
            ) {
                Log.d("BiometricsApi", "biometric none enrolled")
                response?.askUserForPermission()
            }
        }
    }
}

interface BiometricResponse {
    fun onBiometricSuccess(response: String?)
    fun onBiometricFailure()
    fun onBiometricError(errCode: String, errResponse: String)
    fun askUserForPermission()
}