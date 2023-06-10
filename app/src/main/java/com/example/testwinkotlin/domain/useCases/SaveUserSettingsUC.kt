package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.domain.model.UserSettingsModel
import com.example.testwinkotlin.domain.model.toJson
import com.example.testwinkotlin.domain.repository.IUserRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SaveUserSettingsUC
@Inject constructor(
    private val repository: IUserRepository
) {

    suspend fun invoke(value: UserSettingsModel): Flow<AsyncTaskResponse<Boolean>> {
        return flow {
            try {

                val userSettingsJson = value.toJson()
                val result = repository.saveLocalUserSettings(userSettingsJson)

                if (result.isEmpty()) {

                    val message = "saveUserSettingsUC() -> Error: Los datos no han sido guardados"
                    Log.d("SaveUserSettingsUC", message)

                    emit(AsyncTaskResponse.Error(message = message))
                } else {

                    val message = "saveUserSettingsUC() -> Success: Los datos han sido guardados ${result}"
                    Log.d("SaveUserSettingsUC", message)

                    emit(AsyncTaskResponse.Success(data = true))
                }
            } catch(e: IOException) {

                e.printStackTrace()
                val message = "Couldn't load data"
                Log.d("SaveUserSettingsUC", message)
                emit(AsyncTaskResponse.Error(message))
            }
        }
    }
}