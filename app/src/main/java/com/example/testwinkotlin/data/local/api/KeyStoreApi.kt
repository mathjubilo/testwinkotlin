package com.example.testwinkotlin.data.local.api

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject


interface IKeyStoreApi {

    fun separateTextFromEncData(data: String): String
    fun separateIVFromEncData(data: String): String
    fun getInitializedCipherForEncryption(keyName: String, withAuthorization: Boolean): Cipher
    fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray, withAuthorization: Boolean): Cipher
    fun encrypt(data: String, forKey: String, withBiometricAuth: Boolean): String
    fun decrypt(data: String, forKey: String, withBiometricAuth: Boolean): String
    fun decryptWithExternalCipher(cipher: Cipher, data: String, forKey: String, withBiometricAuth: Boolean): String
    fun getOrCreateSecretKey(keyName: String, withAuthorization: Boolean): SecretKey
    fun createEncodingCipherWithSecretKey(secretKey: SecretKey)
    fun createDecodingCipherWith(secretKey: SecretKey, IV: String)
    fun createGeneralCipher(): Cipher
    fun encryptWithExternalCipher(cipher: Cipher, data: String, forKey: String, withBiometricAuth: Boolean): String
    fun separateTextFromEncDataAndConvertToByteArray(data: String): ByteArray
    fun separateIVFromEncDataAndConvertToByteArray(data: String): ByteArray
}

class KeyStoreApi
@Inject constructor(

): IKeyStoreApi {

    private val KEY_SIZE: Int = 256
    val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES


    @RequiresApi(Build.VERSION_CODES.R)
    override fun getInitializedCipherForEncryption(keyName: String, withAuthorization: Boolean): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName, withAuthorization)
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        }  catch (e: UserNotAuthenticatedException) {
            Log.d("KeyStoreApi", "Can't encrypt because user isn't authenticated")
        }
        return cipher
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray, withAuthorization: Boolean): Cipher {

        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName, withAuthorization)
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        } catch (e: UserNotAuthenticatedException) {
            Log.d("KeyStoreApi", "Can't decrypt because user isn't authenticated")
        }
        return cipher
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun encrypt(data: String, forKey: String, withBiometricAuth: Boolean): String {

        val cipher = getInitializedCipherForEncryption(forKey, withBiometricAuth)
        val cipherText = cipher.doFinal(data.toByteArray(Charset.forName("UTF-8")))

        return Base64.encodeToString(
            cipherText,
            Base64.DEFAULT
        ) + "|" + Base64.encodeToString(
            cipher.iv,
            Base64.DEFAULT
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun decrypt(data: String, forKey: String, withBiometricAuth: Boolean): String {

        val encryptedText = separateTextFromEncData(data)
        val cipherIvText = separateIVFromEncData(data)

        val encryptedTextByteArray = Base64.decode(
            encryptedText.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        val cipherIvByteArray = Base64.decode(
            cipherIvText.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        val cipher: Cipher = getInitializedCipherForDecryption(forKey, cipherIvByteArray, true)
        val plainText = cipher.doFinal(encryptedTextByteArray)

        return String(plainText, Charset.forName("UTF-8"))
    }

    override fun decryptWithExternalCipher(cipher: Cipher, data: String, forKey: String, withBiometricAuth: Boolean): String {

        val encryptedTextByteArray = separateTextFromEncDataAndConvertToByteArray(data)

        val plainText = cipher.doFinal(encryptedTextByteArray)
        return String(plainText, Charset.forName("UTF-8"))
    }


    private fun getCipher(): Cipher {

        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getOrCreateSecretKey(keyName: String, withAuthorization: Boolean): SecretKey {

        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        val paramsBuilder = KeyGenParameterSpec.Builder(keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(withAuthorization)
            // Cancel the key validity when we force to authenticate
            setInvalidatedByBiometricEnrollment(false)
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setUserAuthenticationParameters(
                60,
                KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL
            )
            //}
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    override fun createEncodingCipherWithSecretKey(secretKey: SecretKey) {

        var encCipher: Cipher?= null
        encCipher = createGeneralCipher()
        encCipher?.init(Cipher.ENCRYPT_MODE, secretKey)
    }

    override fun createDecodingCipherWith(secretKey: SecretKey, IV: String) {

        var decCipher: Cipher?= null
        decCipher = createGeneralCipher()
        decCipher?.init(
            Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(
                Base64.decode(
                    IV.toByteArray(Charsets.UTF_8),
                    Base64.DEFAULT
                )
            )
        )
    }

    override fun createGeneralCipher(): Cipher {

        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
    }

    override fun separateTextFromEncData(data: String): String {

        return data.split("|")[0].replace("\n", "")
    }

    override fun separateIVFromEncData(data: String): String {

        return data.split("|")[1].replace("\n", "")
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun encryptWithExternalCipher(cipher: Cipher, data: String, forKey: String, withBiometricAuth: Boolean): String {

        val cipherText = cipher.doFinal(data.toByteArray(Charset.forName("UTF-8")))

        return Base64.encodeToString(
            cipherText,
            Base64.DEFAULT
        ) + "|" + Base64.encodeToString(
            cipher.iv,
            Base64.DEFAULT
        )
    }

    override fun separateTextFromEncDataAndConvertToByteArray(data: String): ByteArray {

        val separated =  data.split("|")[0].replace("\n", "")
        val encryptedTextInByteArray = Base64.decode(
            separated.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        return encryptedTextInByteArray
    }


    override fun separateIVFromEncDataAndConvertToByteArray(data: String): ByteArray {

        val separated = data.split("|")[1].replace("\n", "")
        val encryptedTextInByteArray = Base64.decode(
            separated.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )

        return encryptedTextInByteArray
    }
}
