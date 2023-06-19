package com.example.androidapp.Room

import com.example.androidapp.Entity.Fahrzeug

//  Datenklasse, die die relevanten Datenfelder enthält, die den Zustand der Benutzeroberfläche beschreiben.
data class FahrzeugState(
    val fahrzeuge: List<Fahrzeug> = emptyList(),
    val marke: String = "",
    val name: String = "",
    val ps: Int = 0,
    val preis: Int = 0,
    val standort: String = "",
    val ausstattung: String = "",
    val zeitraum: String = "",
    val fotoURL: String = "",
    val isAddingFahrzeug: Boolean = false,
    val sortType: SortType = SortType.MARKE
)
