package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.domain.model.UserInfoModel
import com.example.testwinkotlin.domain.model.toJson
import com.example.testwinkotlin.domain.repository.IUserRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject


class SaveUserInfoUC
@Inject constructor(
    private val repository: IUserRepository
) {

    suspend fun invoke(value: UserInfoModel): Flow<AsyncTaskResponse<Boolean>> {
        return flow {
            try {

                val userInfoJson = value.toJson()
                val result = repository.saveLocalUserInfo(userInfoJson)

                if (result.isEmpty()) {

                    val message = "saveUserInfoUC() -> Error: Los datos no han sido guardados"
                    Log.d("SaveUserInfoUC", message)

                    emit(AsyncTaskResponse.Error(message = message))
                } else {

                    val message = "saveUserInfoUC() -> Success: Los datos han sido guardados ${result}"
                    Log.d("SaveUserInfoUC", message)

                    emit(AsyncTaskResponse.Success(data = true))
                }
            } catch(e: IOException) {

                e.printStackTrace()
                val message = "Couldn't load data"
                Log.d("SaveUserInfoUC", message)
                emit(AsyncTaskResponse.Error(message))
                null
            }
        }
    }
}