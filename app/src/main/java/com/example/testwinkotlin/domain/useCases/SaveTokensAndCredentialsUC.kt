package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.data.repository.TokensManagementRepository
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SaveTokensAndCredentialsUC
@Inject constructor(
    private val repository: TokensManagementRepository
) {

    suspend fun invoke(value: String): Flow<AsyncTaskResponse<Boolean>> {

        return flow {

            try {

                val result = repository.saveEncryptedTokensToDevice(value)
                if (result.isEmpty()) {

                    val message = "saveTokensToDevice() -> Error: Los datos no han sido guardados"
                    Log.d("SaveTokensAndCredentialsUC", message)

                    emit(AsyncTaskResponse.Error(message = message))
                } else {

                    val message = "saveTokensToDevice() -> Success: Los datos han sido guardados"
                    Log.d("SaveTokensAndCredentialsUC", message)

                    emit(AsyncTaskResponse.Success(data = true))
                }
            } catch(e: IOException) {

                e.printStackTrace()
                val message = "Couldn't load data"
                Log.d("SaveTokensAndCredentialsUC", message)
                emit(AsyncTaskResponse.Error(message))
                null
            }
        }
    }
}