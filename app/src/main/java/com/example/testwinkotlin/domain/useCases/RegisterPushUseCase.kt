package com.example.testwinkotlin.domain.useCases

import com.example.testwinkotlin.domain.repository.IPushNotificationsRepository
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class RegisterPushUseCase
@Inject constructor(
    //private val repository: IPushNotificationsRepository
) {
    fun invoke(alias: String): Flow<AsyncTaskResponse<Boolean>> {

        return flow {
            /*try {
                val result = repository.register(alias = alias)
                result?.let {
                    emit(AsyncTaskResponse.Success(data = true))
                }
            } catch(e: IOException) {
                e.printStackTrace()
                val message = ""
                emit(AsyncTaskResponse.Error(message = message))
            }*/
        }
    }
}