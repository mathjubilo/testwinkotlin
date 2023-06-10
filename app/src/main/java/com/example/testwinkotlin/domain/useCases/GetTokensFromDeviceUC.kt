package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetTokensFromDeviceUC
@Inject constructor(
    private val repository: ITokensManagementRepository
) {

    suspend fun invoke(): Flow<AsyncTaskResponse<String>> {

        return flow {
            try {
                val result = repository.getTokensFromDevice()
                result?.let {
                    val encryptedString = it
                    if (encryptedString.isEmpty()) {
                        val message = "No hay datos guardados"
                        Log.d("GetTokensFromDeviceUC", message)
                        emit(AsyncTaskResponse.Error(message = message))
                    } else {
                        emit(AsyncTaskResponse.Success(data = encryptedString))
                    }
                }
            } catch(e: IOException) {
                e.printStackTrace()
                val message = "No hay datos guardados"
                emit(AsyncTaskResponse.Error(message))
            } catch (e: NegativeArraySizeException) {
                val message = "Couldn't load data"
                emit(AsyncTaskResponse.Error(message))
            } catch (e: IndexOutOfBoundsException) {
                val message = "No se ha podido separar el IV de la string encriptada"
                emit(AsyncTaskResponse.Error(message))
            } catch (e: ClassCastException) {
                val message = "Los datos encontrados no es una string"
                emit(AsyncTaskResponse.Error(message))
            }
        }
    }
}