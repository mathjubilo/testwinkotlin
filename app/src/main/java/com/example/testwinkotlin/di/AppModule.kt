package com.example.testwinkotlin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.testwinkotlin.data.local.api.DataStoreApi
import com.example.testwinkotlin.data.local.api.IDataStoreApi
import com.example.testwinkotlin.data.local.api.IKeyStoreApi
import com.example.testwinkotlin.data.local.api.KeyStoreApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.repository.TokensManagementRepository
import com.example.testwinkotlin.data.repository.LoginRepository
import com.example.testwinkotlin.data.repository.LogoutRepository
import com.example.testwinkotlin.data.repository.UserRepository
import com.example.testwinkotlin.domain.repository.ITokensManagementRepository
import com.example.testwinkotlin.domain.repository.ILoginRepository
import com.example.testwinkotlin.domain.repository.ILogoutRepository
import com.example.testwinkotlin.domain.repository.IUserRepository
import com.example.testwinkotlin.utils.interceptors.AuthorizationInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "winStorage"
    )

    @Provides
    @Singleton
    fun provideWinApi(client: OkHttpClient): IWinApi {

        return Retrofit.Builder()
            .baseUrl(IWinApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthorizationInterceptor {

        return AuthorizationInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthorizationInterceptor/*, tokenRefreshAuthenticator: TokenRefreshAuthenticator*/): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            /*.authenticator(tokenRefreshAuthenticator)*/
            .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class LoginRepositoryModule {

        @Binds
        @Singleton
        abstract fun bindLoginRepository(
            repository: LoginRepository
        ): ILoginRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class LaunchScreenRepositoryModule {

        @Binds
        @Singleton
        abstract fun bindLaunchScreenRepository(
            repository: TokensManagementRepository
        ): ITokensManagementRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class UserRepositoryModule {

        @Binds
        @Singleton
        abstract fun bindUserRepository(
            repository: UserRepository
        ): IUserRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class LogoutRepositoryModule {

        @Binds
        @Singleton
        abstract fun bindLogoutRepository(
            repository: LogoutRepository
        ): ILogoutRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class KeyStoreApiModule {

        @Binds
        @Singleton
        abstract fun bindKeyStoreApi(
            keyStoreApi: KeyStoreApi
        ): IKeyStoreApi
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DataStoreApiModule {

        @Binds
        @Singleton
        abstract fun bindDataStoreApi(
            dataStoreApi: DataStoreApi
        ): IDataStoreApi
    }

    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }
}
