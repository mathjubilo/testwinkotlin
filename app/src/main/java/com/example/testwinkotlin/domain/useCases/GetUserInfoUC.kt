package com.example.testwinkotlin.domain.useCases

import com.example.testwinkotlin.data.remote.dto.user.toUserInfoModel
import com.example.testwinkotlin.domain.model.UserInfoModel
import com.example.testwinkotlin.domain.repository.IUserRepository
import com.example.testwinkotlin.utils.AsyncTaskResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserInfoUC @Inject constructor(
private val repository: IUserRepository
) {
    suspend fun invoke(): Flow<AsyncTaskResponse<UserInfoModel>> {
        return flow {
            try {

                repository.getUserInfo().let {
                    response ->
                    if (response?.code() == 200) {
                        response?.body()?.data?.let { userInfoData ->

                            val userInfo = userInfoData.toUserInfoModel()
                            emit(AsyncTaskResponse.Success(userInfo))
                        }
                    } else {

                        emit(AsyncTaskResponse.Error("HTTP Error: ${response?.code()}"))
                    }
                }
            } catch(e: IOException) {

                e.printStackTrace()
                emit(AsyncTaskResponse.Error("IOException"))
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

