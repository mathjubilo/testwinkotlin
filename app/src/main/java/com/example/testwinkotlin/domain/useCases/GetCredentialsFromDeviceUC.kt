package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCredentialsFromDeviceUC
@Inject constructor(
    private val repository: ITokensManagementRepository
) {

    suspend fun invoke(): Flow<AsyncTaskResponse<String>> {

        return flow {
            try {
                val result = repository.getCredentialsFromDevice()
                result?.let {
                    val encryptedString = it
                    if (encryptedString.isEmpty()) {
                        val message = "No hay datos guardados"
                        Log.d("GetCredentialsFromDeviceUC", message)
                        emit(AsyncTaskResponse.Error(message = message))
                    } else {
                        emit(AsyncTaskResponse.Success(data = encryptedString))
                    }
                }
            } catch(e: IOException) {
                e.printStackTrace()
                val message = "No hay datos guardados"
                emit(AsyncTaskResponse.Error(message))
            }
        }
    }
}