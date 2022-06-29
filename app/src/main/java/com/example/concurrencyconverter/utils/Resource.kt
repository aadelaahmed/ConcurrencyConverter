package com.example.concurrencyconverter.utils

sealed class Resource<T> (val data : T? = null,val message : String? = null)
{
     class SUCCESS<T>( data : T) : Resource<T> (data)
     class ERROR<T> (message: String) : Resource<T>(message = message)
}