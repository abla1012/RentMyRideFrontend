package com.example.androidapp.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidapp.Entity.Fahrzeug

//  Diese Klasse wird zur Erstellung und Verwaltung der Fahrzeug-Datenbank verwendet.
/*
Die @Database-Annotation wird verwendet, um anzugeben, dass es sich um eine Room-Datenbank handelt.
Das entities-Attribut gibt an, welche Entitäten in der Datenbank gespeichert werden sollen.
Das version-Attribut gibt die Version der Datenbank an. Wenn sich die Datenbankstruktur ändert,
kann die Versionsnummer erhöht werden, um Migrationen zu ermöglichen.
 */
@Database(
    entities = [Fahrzeug::class],
    version = 1
)
abstract class FahrzeugDatabase : RoomDatabase() {

    // Ermöglicht den Zugriff auf den DAO (Data Access Object),
    // der die Datenbankoperationen für die Fahrzeug-Entität definiert.
    abstract val dao: FahrzeugDao
}