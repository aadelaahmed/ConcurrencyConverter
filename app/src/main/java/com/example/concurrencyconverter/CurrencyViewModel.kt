package com.example.concurrencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concurrencyconverter.data.model.Rates
import com.example.concurrencyconverter.repository.MainRepository
import com.example.concurrencyconverter.utils.DispatcherProvider
import com.example.concurrencyconverter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    val repository: MainRepository,
    val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class SUCCESS(val resultStr: String) : CurrencyEvent()
        class FAILURE(val errorStr: String) : CurrencyEvent()
        object LOADING : CurrencyEvent()
        object INITIAL : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.INITIAL)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.FAILURE(errorStr = "error in value format")
            return
        }
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.LOADING
            when (val currenyResponse = repository.getRates(fromCurrency)) {
                is Resource.ERROR -> CurrencyEvent.FAILURE(currenyResponse.message!!)
                is Resource.SUCCESS -> {
                    val rates = currenyResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.FAILURE("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.SUCCESS(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}