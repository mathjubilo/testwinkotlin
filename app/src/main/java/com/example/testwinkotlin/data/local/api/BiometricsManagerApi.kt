package com.example.testwinkotlin.data.local.api

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.R)
class BiometricsManagerApi (
    val mainActivity: FragmentActivity,
    val secretKeyName: String,
    var stringToEncrypt: String,
    var encryptedString: String,
    val response: BiometricResponse
) {

    private var doEncryption = true
    private var executor: Executor
    private var biometricPrompt: BiometricPrompt
    private var promptInfoAskingBiometricsOrDeviceLock: BiometricPrompt.PromptInfo
    private var promptInfoWithFaceRecognitionWhenBiometricsWasAlreadyAuthenticated: BiometricPrompt.PromptInfo
    var decryptedString = ""

    init {

        // Comprueba que la autenticación biométrica esté disponible
        val biometricManager = BiometricManager.from(mainActivity)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("BiometricsManagerApi", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("BiometricsManagerApi", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("BiometricsManagerApi", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                mainActivity.startActivity(enrollIntent, null)
            }
        }



        // Los callback despues de la autenticación
        executor = ContextCompat.getMainExecutor(mainActivity)
        biometricPrompt = BiometricPrompt(
            mainActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("BiometricsManagerApi", "Authentication error: $errString")

                    when(errorCode) {
                        BiometricPrompt.ERROR_CANCELED -> {

                        }
                        BiometricPrompt.ERROR_LOCKOUT -> {

                        }
                        BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {

                        }
                        BiometricPrompt.ERROR_USER_CANCELED -> {

                        }
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {

                        }
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    when(doEncryption) {
                        true -> {
                            result.cryptoObject?.cipher?.let { cipher ->
                                Log.d("BiometricsManagerApi", "Authentication succeeded, doing encryption")

                                try {

                                    encryptedString = encrypt(
                                        data = stringToEncrypt,
                                        cipher = cipher
                                    )

                                    Log.d("BiometricsManagerApi", "Encriptado con suceso: ${encryptedString}")
                                    response.onBiometricSuccess(encryptedString)
                                } catch (e: InvalidKeyException) {
                                    Log.e("BiometricsManagerApi", "Key is invalid in result: ${e.message}")
                                    //biometricPrompt.authenticate(promptInfo)
                                } catch (e: UserNotAuthenticatedException) {
                                    Log.d("BiometricsManagerApi", "The key's validity timed out.")
                                    //biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
                                }
                                Log.d("BiometricsManagerApi", "Encryption result ${encryptedString}")
                            }
                        }

                        false -> {
                            result.cryptoObject?.cipher?.let { cipher ->
                                try {
                                    decryptedString = decrypt(
                                        data = encryptedString,
                                        cipher = cipher
                                    )
                                } catch (e: InvalidKeyException) {
                                    Log.e("BiometricsManagerApi", "Key is invalid. ${e.message}")
                                } catch (e: UserNotAuthenticatedException) {
                                    Log.d("BiometricsManagerApi", "The key's validity timed out.  ${e.message}")
                                    //biometricPrompt.authenticate(promptInfo)
                                }
                            }
                        }
                    }

                    // Detect what authentication method was used
                    when(result.authenticationType) {
                        BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> {

                        }
                        BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> {

                        }
                        BiometricPrompt.AUTHENTICATION_RESULT_TYPE_UNKNOWN -> {

                        }
                    }
                    // Cómo autenticar solo con credenciales biométricas, paso 3
                    /*val encryptedInfo: ByteArray? = result.cryptoObject?.cipher?.doFinal(
                        // plaintext-string text is whatever data the developer would like
                        // to encrypt. It happens to be plain-text in this example, but it
                        // can be anything
                        stringToEncrypt.toByteArray(Charset.defaultCharset())
                    )
                    Log.d("BiometricsManagerApi", "Encrypted information: " +
                            Arrays.toString(encryptedInfo))*/
                    // ==================================
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("BiometricsManagerApi", "Authentication failed")
                }
            })


        // Lets the user authenticate using either a Class 3 biometric or
        // their lock screen credential (PIN, pattern, or password).
        promptInfoAskingBiometricsOrDeviceLock = BiometricPrompt.PromptInfo.Builder()
            .setTitle("WIN Biometric")
            .setSubtitle("Who are you?")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        // Lets the user authenticate without performing an action, such as pressing a
        // button, after their biometric credential is accepted.
        promptInfoWithFaceRecognitionWhenBiometricsWasAlreadyAuthenticated = BiometricPrompt.PromptInfo.Builder()
            .setTitle("WIN Biometric")
            .setSubtitle("Who are you?")
            .setNegativeButtonText("Use account password")
            .setConfirmationRequired(false)
            .build()

        /*promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()*/

        /*createSecretKeyWithValidityTimeout(
            secretKey = secretKeyName
        )*/


        // Cómo autenticar y encriptar solo con credenciales biométricas
        //authenticateAndEncryptOnlyWithBiometricCredentials(biometricPrompt, promptInfoAskingBiometricsOrDeviceLock)


        // Autentica con credenciales biométricas o de pantalla de bloqueo
        //authenticateAndEncryptWithBiometricOrDeviceLockCredentials(biometricPrompt, promptInfoAskingBiometricsOrDeviceLock)
    }



    fun authenticateAndEncryptWithBiometricOrDeviceLockCredentials(
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo
    ) {
        // Autentica con credenciales biométricas o de pantalla de bloqueo
        // 1. Genera una clave que use la siguiente configuración KeyGenParameterSpec. Clave que solo ser autilizada 60 segundos:
        createSecretKeyWithValidityTimeout(
            secretKey = secretKeyName
        )

        // 2. Dentro de un período de VALIDITY_DURATION_SECONDS después de que se autentique el usuario, encripta la información sensible:
        // Exceptions are unhandled for getCipher() and getSecretKey().
        val cipher = getCipher()

        val secretKey = getSecretKey()

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            /*val encryptedInfoByteArray: ByteArray = cipher.doFinal(
                // plaintext-string text is whatever data the developer would
                // like to encrypt. It happens to be plain-text in this example,
                // but it can be anything
                stringToEncrypt.toByteArray(Charset.defaultCharset())
            )
            Log.d("BiometricsManagerApi", "Encrypted information: " +
                    Arrays.toString(encryptedInfoByteArray))*/

            encryptedString = encrypt(
                data = stringToEncrypt,
                cipher = cipher
            )

            Log.d("BiometricsManagerApi", "Encriptado con suceso: ${encryptedString}")
        } catch (e: InvalidKeyException) {
            Log.e("BiometricsManagerApi", "Key is invalid: ${e.message}")
            biometricPrompt.authenticate(promptInfo)
        } catch (e: UserNotAuthenticatedException) {
            Log.d("BiometricsManagerApi", "The key's validity timed out.")
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun createSecretKeyWithValidityTimeout(secretKey: String) {
        generateSecretKey(KeyGenParameterSpec.Builder(
            secretKey,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationParameters(
                /*
                Si este timeout esta a cero entonces va a pedir autenticación siempre que se
                utilice esa clave. Pero si tiene un tiempo, solo se va a pedir la autenticación
                cuando este timeout acabe
                */
                60,
                KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL)
            .build())
    }


    fun authenticateAndEncryptOnlyWithBiometricCredentials(
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo
    ) {
        // Cómo autenticar y encriptar solo con credenciales biométricas

        // 1. Genera una clave que use la siguiente configuración KeyGenParameterSpec:
        generateSecretKey(KeyGenParameterSpec.Builder(
            secretKeyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            // Invalidate the keys if the user has registered a new biometric
            // credential, such as a new fingerprint. Can call this method only
            // on Android 7.0 (API level 24) or higher. The variable
            // "invalidatedByBiometricEnrollment" is true by default.
            .setInvalidatedByBiometricEnrollment(true)
            .build())

        // 2. Inicia un flujo de trabajo de autenticación biométrica que incorpore un algoritmo de cifrado:
        //biometricLoginButton.setOnClickListener
        // Exceptions are unhandled within this snippet.
        val cipher = getCipher()

        val secretKey = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipher)
        )

        // 3. El paso tres está en el callback arriba
    }

    fun authenticateToEncrypt() {
        doEncryption = true
        // Exceptions are unhandled within this snippet.
        try {
            generateSecretKey(KeyGenParameterSpec.Builder(
                secretKeyName,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                // Invalidate the keys if the user has registered a new biometric
                // credential, such as a new fingerprint. Can call this method only
                // on Android 7.0 (API level 24) or higher. The variable
                // "invalidatedByBiometricEnrollment" is true by default.
                .setInvalidatedByBiometricEnrollment(true)
                .build())

            val cipher = getCipher()
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            biometricPrompt.authenticate(
                promptInfoAskingBiometricsOrDeviceLock,
                BiometricPrompt.CryptoObject(cipher)
            )
        } catch (e: InvalidKeyException) {
            Log.e("BiometricsManagerApi", "Key is invalid on method authenticateToEncrypt(). ${e.message}")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("BiometricsManagerApi", "The key's validity timed out.  ${e.message}")
            //biometricPrompt.authenticate(promptInfo)
        }
    }

    fun authenticateToDecrypt() {
        doEncryption = false
    }

    // Keystore methods
    @RequiresApi(Build.VERSION_CODES.M)
    fun encrypt(cipher: Cipher, data: String): String {

        val encryptedDataByteArray = cipher.doFinal(data.toByteArray(Charset.forName("UTF-8")))

        val encryptedDataAndIvConvertedToString = Base64.encodeToString(
            encryptedDataByteArray,
            Base64.DEFAULT
        ) + "|" + Base64.encodeToString(
            cipher.iv,
            Base64.DEFAULT
        )

        return encryptedDataAndIvConvertedToString
    }

    fun decrypt(cipher: Cipher, data: String): String {

        val encryptedDataConvertedToByteArray = separateTextFromEncryptedStringAndConvertToByteArray(data)

        val decryptedDataByteArray = cipher.doFinal(encryptedDataConvertedToByteArray)

        val decryptedDataConvertedToString = String(decryptedDataByteArray, Charset.forName("UTF-8"))

        return decryptedDataConvertedToString
    }

    fun separateTextFromEncryptedString(data: String): String {

        return data.split("|")[0].replace("\n", "")
    }
    fun separateTextFromEncryptedStringAndConvertToByteArray(data: String): ByteArray {

        val separated =  data.split("|")[0].replace("\n", "")
        val encryptedTextInByteArray = Base64.decode(
            separated.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        return encryptedTextInByteArray
    }

    fun separateIVFromEncryptedString(data: String): String {

        return data.split("|")[1].replace("\n", "")
    }
    fun separateIVFromEncryptedStringAndConvertToByteArray(data: String): ByteArray {

        val separated = data.split("|")[1].replace("\n", "")
        val encryptedTextInByteArray = Base64.decode(
            separated.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        return encryptedTextInByteArray
    }

    // Usa una solución criptográfica que dependa de la autenticación
    fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(secretKeyName, null) as SecretKey
    }

    fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    fun encryptWithoutAskingAuth() {
        try {

            val cipher = getCipher()
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            encryptedString = encrypt(
                data = stringToEncrypt,
                cipher = cipher
            )

            Log.d("BiometricsManagerApi", "Encriptado con suceso: ${encryptedString}")
            response.onBiometricSuccess(encryptedString)
        } catch (e: InvalidKeyException) {
            Log.e("BiometricsManagerApi", "Key is invalid in result: ${e.message}")
            //biometricPrompt.authenticate(promptInfo)
        } catch (e: UserNotAuthenticatedException) {
            Log.d("BiometricsManagerApi", "The key's validity timed out.")
            //biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }
}
