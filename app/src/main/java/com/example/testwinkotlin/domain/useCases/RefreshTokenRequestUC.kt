package com.example.testwinkotlin.domain.useCases

import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.domain.model.TokensAndCredentialsModel
import com.example.testwinkotlin.utils.AsyncTaskResponse
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RefreshTokenRequestUC
@Inject constructor(
    private val repository: ITokensManagementRepository
) {

    suspend fun invoke(refreshToken: String): Flow<AsyncTaskResponse<TokensAndCredentialsModel>> {
        return flow {
            try {
                val renewTokenResponseObject = repository.refreshToken(
                    refreshToken = refreshToken
                )
                val responseCode = renewTokenResponseObject.code();
                if (responseCode == 500) {
                    emit(AsyncTaskResponse.Error("Error 500"))
                } else if (responseCode == 401) {
                    emit(AsyncTaskResponse.Error("Error 401 -> Refresh token expirated"))
                } else if (responseCode == 200 ){
                    val updatedAccessToken = renewTokenResponseObject.headers().get("access-token")
                    val updatedRefreshToken = renewTokenResponseObject.headers().get("refresh-token")
                    val tokensObject = TokensAndCredentialsModel(
                        accessToken = updatedAccessToken ?: "",
                        refreshToken = updatedRefreshToken ?: "",
                        username = IWinApi.USERNAME,
                        password = IWinApi.PASSWORD
                    )
                    emit(AsyncTaskResponse.Success(tokensObject))
                }
            } catch(e: IOException) {
                e.printStackTrace()
                emit(AsyncTaskResponse.Error("refreshToken IOException Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(AsyncTaskResponse.Error("refreshToken HttpException "+e.response()))
                null
            }
        }
    }
}