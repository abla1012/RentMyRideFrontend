package com.example.androidapp.Room

import com.example.androidapp.Entity.Fahrzeug

sealed interface FahrzeugEvent {
    object SaveFahrzeug: FahrzeugEvent
    data class SetMarke(val marke: String): FahrzeugEvent
    data class SetName(val name: String): FahrzeugEvent
    data class SetPS(val ps: Int): FahrzeugEvent
    data class SetPreis(val preis: Int): FahrzeugEvent
    data class SetStandort(val standort: String): FahrzeugEvent
    data class SetAusstattung(val ausstattung: String): FahrzeugEvent
    data class SetZeitraum(val zeitraum: String): FahrzeugEvent
    data class SetFotoURL(val fotoURL: String): FahrzeugEvent
    object ShowDialog: FahrzeugEvent
    object HideDialog: FahrzeugEvent
    data class SortFahrzeuge(val sortType: SortType): FahrzeugEvent
    data class DeleteFahrzeug(val fahrzeug: Fahrzeug): FahrzeugEvent
}