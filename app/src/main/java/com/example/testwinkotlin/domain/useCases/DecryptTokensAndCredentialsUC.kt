package com.example.testwinkotlin.domain.useCases

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import com.example.testwinkotlin.domain.model.TokensAndCredentialsModel
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.security.KeyStoreException
import javax.crypto.IllegalBlockSizeException
import javax.inject.Inject


class DecryptTokensAndCredentialsUC
@Inject constructor(
    private val repository: ITokensManagementRepository
) {

    suspend fun invoke(encryptedString: String): Flow<AsyncTaskResponse<String>> {

        return flow {

            try {

                Log.d("DecryptTokensAndCredentialsUC","Json encrypted string $encryptedString")
                val decryptedString = repository.decryptTokensAndCredentials(encryptedString)

                if (decryptedString.isEmpty()) {
                    val message = "encryptTokens() -> Ha ocurrido un error: La string encriptada está vacia"
                    Log.d("DecryptTokensAndCredentialsUC", message)
                    emit(AsyncTaskResponse.Error(message = message))
                } else {

                    emit(AsyncTaskResponse.Success(data = decryptedString))
                }
            } catch(e: IOException) {
                e.printStackTrace()
                emit(AsyncTaskResponse.Error("Couldn't load data"))
                null
            } catch(e: KeyStoreException) {
                e.printStackTrace()
                //emit(AsyncTaskResponse.Error("Usuario no ha autenticado", data = "notAuthenticated"))
            } catch (e: IllegalBlockSizeException){
                emit(AsyncTaskResponse.Error("El IV no coincide", data = "ivIncorrect"))
            } catch (e: UserNotAuthenticatedException) {
                emit(AsyncTaskResponse.Error("Usuario no ha autenticado", data = "notAuthenticated"))
            } catch (e: IllegalStateException) {
                emit(AsyncTaskResponse.Error("Usuario no ha autenticado", data = "cipherNotInitialized"))
            }
        }
    }
}
