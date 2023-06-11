package com.example.androidapp.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidapp.Entity.Fahrzeug


@Database(
    entities = [Fahrzeug::class],
    version = 1
)
abstract class FahrzeugDatabase: RoomDatabase() {

    abstract val dao: FahrzeugDao
}