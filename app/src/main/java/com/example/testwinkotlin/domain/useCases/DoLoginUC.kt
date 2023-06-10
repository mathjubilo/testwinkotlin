package com.example.testwinkotlin.domain.useCases

import com.example.testwinkotlin.data.remote.dto.login.LoginRequestObject
import com.example.testwinkotlin.domain.model.TokensAndCredentialsModel
import com.example.testwinkotlin.utils.AsyncTaskResponse
import com.example.testwinkotlin.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DoLoginUC
@Inject constructor(
    private val repository: ILoginRepository
) {

    suspend fun invoke(username: String, password: String): Flow<AsyncTaskResponse<TokensAndCredentialsModel>> {

        return flow {

            try {

                val loginRequestObject = LoginRequestObject(username, password)
                val request = repository.login(loginRequestObject)
                var loginResponse = TokensAndCredentialsModel("", "", "", "")

                if (request.code() == 200) {

                    val accessToken = request.headers().get("access-token") ?: ""
                    val renewToken = request.headers().get("refresh-token") ?: ""
                    loginResponse = TokensAndCredentialsModel(accessToken, renewToken, username, password)

                    emit(AsyncTaskResponse.Success(loginResponse))
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