package com.example.myandroidtemplate.di
import com.example.myandroidtemplate.data.remote.api.AuthApi
import com.example.myandroidtemplate.data.remote.AuthInterceptor
import com.example.myandroidtemplate.data.remote.mock.MockApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://your.api.url/"

    // ---------- MAIN OKHTTP (with interceptor)
    @Provides
    @Singleton
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        mockApiInterceptor: MockApiInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(mockApiInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    // ---------- REFRESH OKHTTP (NO interceptor)
    @Provides
    @Singleton
    @RefreshApi
    fun provideRefreshOkHttp(): OkHttpClient =
        OkHttpClient.Builder().build()

    // ---------- MAIN RETROFIT
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ---------- REFRESH RETROFIT
    @Provides
    @Singleton
    @RefreshApi
    fun provideRefreshRetrofit(
        @RefreshApi okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ---------- MAIN AUTH API
    @Provides
    @Singleton
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi =
        retrofit.create(AuthApi::class.java)

    // ---------- REFRESH AUTH API
    @Provides
    @Singleton
    @RefreshApi
    fun provideRefreshAuthApi(
        @RefreshApi retrofit: Retrofit
    ): AuthApi =
        retrofit.create(AuthApi::class.java)
}
