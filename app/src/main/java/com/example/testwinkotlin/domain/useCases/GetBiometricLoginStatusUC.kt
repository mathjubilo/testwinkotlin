package com.example.testwinkotlin.domain.useCases
import android.util.Log
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
class GetBiometricLoginStatusUC
@Inject constructor(
    private val repository: ILoginRepository
) {
    suspend fun invoke(): Flow<AsyncTaskResponse<Boolean>> {
        return flow {
            try {
                val result = repository.getBiometricLoginStatus()
                if (result) {
                    val message = "getBiometricLoginStatusUC() -> Error:No hay datos"
                    Log.d("getBiometricLoginStatusUC", message)
                    emit(AsyncTaskResponse.Success(data = result))
                } else {
                    val message = "getBiometricLoginStatusUC() -> False: ${result}"
                    Log.d("getBiometricLoginStatusUC", message)
                    emit(AsyncTaskResponse.Error(message = message))
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