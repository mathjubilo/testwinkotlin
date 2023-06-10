package com.example.testwinkotlin.domain.useCases

import android.util.Log
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.data.remote.dto.logout.LogoutRequestObject
import com.example.testwinkotlin.domain.model.TokensAndCredentialsModel
import com.example.testwinkotlin.domain.repository.ILogoutRepository
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LogoutUC
@Inject constructor(
    private val repository: ILogoutRepository
) {
    suspend fun invoke(): Flow<AsyncTaskResponse<Boolean>> {

        return flow {
            try {

                val logoutRequestObject = LogoutRequestObject(IWinApi.ACCESS_TOKEN)
                val request = repository.logout(logoutRequestObject)

                if (request.code() == 204) {
                    emit(AsyncTaskResponse.Success(true))
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