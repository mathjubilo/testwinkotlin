package com.example.testwinkotlin.domain.useCases

import com.example.testwinkotlin.data.remote.dto.user.toJson
import com.example.testwinkotlin.data.remote.dto.user.toUserSettingsModel
import com.example.testwinkotlin.domain.model.UserSettingsModel
import com.example.testwinkotlin.domain.model.toJson
import com.example.testwinkotlin.domain.repository.IUserRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserSettingsUC
@Inject constructor(
    private val repository: IUserRepository
) {
    suspend fun invoke(): Flow<AsyncTaskResponse<UserSettingsModel>> {
        return flow {
            try {

                val request = repository.getUserSettings()

                if (request.code() == 200) {
                    request.body()?.data?.let { userSettingsResponse ->

                        val userSettings = userSettingsResponse.toUserSettingsModel()
                        println("kork value is ${userSettings.notificationsSettings?.high}")
                        println("userSettings -> ${userSettings.toJson()}")
                        emit(AsyncTaskResponse.Success(userSettings))
                    }
                } else {

                    emit(AsyncTaskResponse.Error("HTTP Error: ${request.code()}"))
                }
            } catch(e: IOException) {

                e.printStackTrace()
                emit(AsyncTaskResponse.Error("IOException"))
                null
            } catch (e: HttpException) {

                e.printStackTrace()

                if (e.code() == 401) {

                    val headers = e.response()?.headers()
                    val body = e.response()?.body()
                    val errorBody = e.response()?.errorBody()

                    emit(AsyncTaskResponse.Error("headers: ${headers} \n error body: ${errorBody} \n body: ${body}"))
                }
            }
        }
    }
}