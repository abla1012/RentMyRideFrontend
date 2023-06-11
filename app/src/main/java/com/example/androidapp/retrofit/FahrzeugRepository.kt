package com.example.androidapp.retrofit

import com.example.androidapp.Entity.Fahrzeug
import com.example.androidapp.retrofit.di.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class FahrzeugRepository {
    private val apiService = ApiClient.apiInterface

    fun getFahrzeuge(): Flow<List<Fahrzeug>> = flow {
        emit(apiService.getFahrzeuge())
    }.flowOn(Dispatchers.IO)

    fun getFahrzeugeOrderByMarke(): Flow<List<Fahrzeug>> = flow {
        emit(apiService.getFahrzeugeOrderByMarke())
    }.flowOn(Dispatchers.IO)

    fun getFahrzeugeOrderByName(): Flow<List<Fahrzeug>> = flow {
        emit(apiService.getFahrzeugeOrderByName())
    }.flowOn(Dispatchers.IO)

    fun getFahrzeugeOrderByPs(): Flow<List<Fahrzeug>> = flow {
        emit(apiService.getFahrzeugeOrderByPs())
    }.flowOn(Dispatchers.IO)

    suspend fun addFahrzeug(fahrzeug: Fahrzeug): Response<String> = apiService.addFahrzeug(fahrzeug)

    suspend fun deleteFahrzeug(id: Int): Response<Unit> = apiService.deleteFahrzeug(id)
}