package com.example.androidapp.Room

import androidx.room.*
import com.example.androidapp.Entity.Fahrzeug
import kotlinx.coroutines.flow.Flow

// Definiert verschiedenen DB-Endpunkte und enthält die Methoden für die Kommunikation mit der DB
@Dao
interface FahrzeugDao {
    // Fügt ein Fahrzeug in die Datenbank ein oder aktualisiert es (Upsert) und gibt die ID der eingefügten/aktualisierten Zeile zurück.
    @Upsert
    suspend fun upsertFahrzeug(fahrzeug: Fahrzeug): Long

    // Löscht ein spezifisches Fahrzeug aus der Datenbank.
    @Delete
    suspend fun deleteFahrzeug(fahrzeug: Fahrzeug)

    // Löscht alle Fahrzeuge aus der Datenbank.
    @Query("DELETE FROM fahrzeug")
    suspend fun deleteAllFahrzeuge()

    // TODO Löschen: Ruft alle Fahrzeuge aus der Datenbank ab und ordnet sie nach 'marke' aufsteigend.
    @Query("SELECT * FROM fahrzeug ORDER BY marke ASC")
    fun getFahrzeugeOrderedByMarke(): Flow<List<Fahrzeug>>

    //  Ruft alle Fahrzeuge aus der Datenbank ab und ordnet sie nach der 'name' aufsteigend.
    @Query("SELECT * FROM fahrzeug ORDER BY name ASC")
    fun getFahrzeugeOrderedByName(): Flow<List<Fahrzeug>>

    // Ruft alle Fahrzeuge aus der Datenbank ab und ordnet sie nach 'ps' aufsteigend.
    @Query("SELECT * FROM fahrzeug ORDER BY ps ASC")
    fun getFahrzeugeOrderedByPS(): Flow<List<Fahrzeug>>
}