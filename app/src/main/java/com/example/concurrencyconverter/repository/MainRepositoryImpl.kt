package com.example.concurrencyconverter.repository

import com.example.concurrencyconverter.data.CurrencyApi
import com.example.concurrencyconverter.data.model.CurrencyResponse
import com.example.concurrencyconverter.utils.Resource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(val api: CurrencyApi) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.SUCCESS(data = body)
            } else
                Resource.ERROR(message = response.message())
        } catch (exception: Exception) {
            Resource.ERROR(message = exception.message ?: "An error occured")
        }
    }
}