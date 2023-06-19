package com.example.androidapp.network

import com.example.androidapp.Entity.Fahrzeug
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Definiert verschiedenen API-Endpunkte und enthält die Methoden für die Kommunikation mit der API
// HTTP-Anfragen, URL-Pfade, Query-Parameter, Request-Body, Header usw.
interface ApiInterface {
    companion object {
        // TODO | cmd -> ipconfig -> ipv4 kopieren und ersetzen
        const val BASE_URL = "http://192.168.178.27:8080/"
    }

    //@GET Endpoint für alle Fahrzeuge
    @GET("api/fahrzeuge")
    suspend fun getFahrzeuge(): List<Fahrzeug>

    //@GET Endpoint für ALle Fahrzeuge sortiert nach Marke
    @GET("api/fahrzeuge?marke=desc")
    suspend fun getFahrzeugeOrderByMarke(): List<Fahrzeug>

    //@GET Endpoint für ALle Fahrzeuge sortiert nach Name
    @GET("api/fahrzeuge?preis=desc")
    suspend fun getFahrzeugeOrderByName(): List<Fahrzeug>

    //@GET Endpoint für ALle Fahrzeuge sortiert nach Preis
    @GET("api/fahrzeuge?ps=desc")
    suspend fun getFahrzeugeOrderByPs(): List<Fahrzeug>

    //@GET("api/fahrzeuge/{id}")
    //suspend fun getFahrzeug(@Path("id") id:String): Fahrzeug

    //@POST Endpoint für das hinzufügen von einem Fahrzeug in das Backend
    @POST("api/fahrzeuge")
    suspend fun addFahrzeug(@Body fahrzeug: Fahrzeug) : Response<String>

    //@DELETE Endpoint für das Löschen eines Fahrzeuges im Bakcend
    @DELETE("api/fahrzeuge/{id}")
    suspend fun deleteFahrzeug(@Path("id") id: Int): Response<Unit>

    //@POST("api/auth")
    //suspend fun loginToBackend(@Header("name") name:String, @Header("password") passwort:String) : Void
}