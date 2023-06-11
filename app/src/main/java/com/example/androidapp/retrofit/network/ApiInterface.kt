package com.example.androidapp.network

import com.example.androidapp.Entity.Fahrzeug
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    companion object {
        //const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val BASE_URL = "http://172.26.32.1:8080/"
    }

    //@GET("posts")
    @GET("api/fahrzeuge")
    suspend fun getFahrzeuge(): List<Fahrzeug>

    //@GET("users/list?sort=desc")
    @GET("api/fahrzeuge?marke=desc")
    suspend fun getFahrzeugeOrderByMarke(): List<Fahrzeug>

    @GET("api/fahrzeuge?preis=desc")
    suspend fun getFahrzeugeOrderByName(): List<Fahrzeug>

    @GET("api/fahrzeuge?ps=desc")
    suspend fun getFahrzeugeOrderByPs(): List<Fahrzeug>

    //@GET("api/fahrzeuge/{id}")
    //suspend fun getFahrzeug(@Path("id") id:String): Fahrzeug

    @POST("api/fahrzeuge")
    suspend fun addFahrzeug(@Body fahrzeug: Fahrzeug) : Response<String>

    @DELETE("api/fahrzeuge/{id}")
    suspend fun deleteFahrzeug(@Path("id") id: Int): Response<Unit>

    //@POST("api/auth")
    //suspend fun loginToBackend(@Header("name") name:String, @Header("password") passwort:String) : Void
}