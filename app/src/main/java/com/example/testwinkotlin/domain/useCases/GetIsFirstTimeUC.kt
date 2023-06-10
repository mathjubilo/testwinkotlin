package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetIsFirstTimeUC
@Inject constructor(
    private val repository: ILoginRepository
) {

    suspend fun invoke(): Flow<AsyncTaskResponse<Boolean>> {
        return flow {
            try {

                val result = repository.getIsFirstTime()

                if (result) {

                    val message = "getIsFirstTime() -> Error:No hay datos"
                    Log.d("GetIsFirstTimeUC", message)

                    emit(AsyncTaskResponse.Error(message = message))
                } else {

                    val message = "getIsFirstTime() -> Success: El valor es ${result}"
                    Log.d("GetIsFirstTimeUC", message)

                    emit(AsyncTaskResponse.Success(data = result))
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