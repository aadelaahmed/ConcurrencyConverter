package com.example.concurrencyconverter.di

import com.example.concurrencyconverter.data.CurrencyApi
import com.example.concurrencyconverter.repository.MainRepository
import com.example.concurrencyconverter.repository.MainRepositoryImpl
import com.example.concurrencyconverter.utils.BASE_URL
import com.example.concurrencyconverter.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Singleton
    @Provides
    fun provideCurrencyApi() : CurrencyApi=
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api : CurrencyApi) : MainRepository
            =  MainRepositoryImpl(api)



    @Singleton
    @Provides
    fun provideDispatchers() : DispatcherProvider =
        object  : DispatcherProvider{
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val unconfined: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }

}