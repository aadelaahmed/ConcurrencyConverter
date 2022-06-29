package com.example.concurrencyconverter.repository

import com.example.concurrencyconverter.data.model.CurrencyResponse
import com.example.concurrencyconverter.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MainRepository {
    suspend fun getRates(base : String) : Resource<CurrencyResponse>
}