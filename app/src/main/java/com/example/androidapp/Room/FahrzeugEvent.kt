package com.example.androidapp.Room

import com.example.androidapp.Entity.Fahrzeug

/*
* Wird genutzt um verschiedene Fahrzeug Ereignisse zu repräsentieren.
 */
sealed interface FahrzeugEvent {
    // Stellt das Ereignis dar, ein Fahrzeug zu speichern.
    object SaveFahrzeug : FahrzeugEvent

    // Stellt das Ereignis dar, die Marke eines Fahrzeugs zu setzen.
    data class SetMarke(val marke: String) : FahrzeugEvent

    // Stellt das Ereignis dar, den Namen eines Fahrzeugs zu setzen.
    data class SetName(val name: String) : FahrzeugEvent

    // Stellt das Ereignis dar, die PS (Leistung) eines Fahrzeugs zu setzen.
    data class SetPS(val ps: Int) : FahrzeugEvent

    // Stellt das Ereignis dar, den Preis eines Fahrzeugs zu setzen.
    data class SetPreis(val preis: Int) : FahrzeugEvent

    // Stellt das Ereignis dar, den Standort eines Fahrzeugs zu setzen.
    data class SetStandort(val standort: String) : FahrzeugEvent

    // Stellt das Ereignis dar, die Ausstattung eines Fahrzeugs zu setzen.
    data class SetAusstattung(val ausstattung: String) : FahrzeugEvent

    // Stellt das Ereignis dar, den Zeitraum eines Fahrzeugs zu setzen.
    data class SetZeitraum(val zeitraum: String) : FahrzeugEvent

    // Stellt das Ereignis dar, die URL des Fotos eines Fahrzeugs zu setzen.
    data class SetFotoURL(val fotoURL: String) : FahrzeugEvent

    // Stellt das Ereignis dar, einen Dialog anzuzeigen.
    object ShowDialog : FahrzeugEvent

    // Stellt das Ereignis dar, einen Dialog auszublenden.
    object HideDialog : FahrzeugEvent

    // Stellt das Ereignis dar, Fahrzeuge nach einem bestimmten Sortierungstyp zu sortieren.
    data class SortFahrzeuge(val sortType: SortType) : FahrzeugEvent

    // Stellt das Ereignis dar, ein bestimmtes Fahrzeug zu löschen.
    data class DeleteFahrzeug(val fahrzeug: Fahrzeug) : FahrzeugEvent
}