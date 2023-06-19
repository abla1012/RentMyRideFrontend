package com.example.androidapp.Room

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.Entity.Fahrzeug
import com.example.androidapp.retrofit.FahrzeugRepository
import com.example.androidapp.retrofit.util.ConvertPicture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

// Trennt die Logik der Benutzeroberfläche von der Geschäftslogik
// (Stellt Daten aus dem Model für die Anzeige in der View bereit
// Verwaltet den Zustand der View)

@OptIn(ExperimentalCoroutinesApi::class)
class FahrzeugViewModel(
    private val dao: FahrzeugDao,
    application: Application
) : AndroidViewModel(application) {

    // Zugriff auf Backendserver
    private val repository = FahrzeugRepository()

    // Definiert den Sortiertyp zu Beginn für die Fahrzeuge
    private val _sortType = MutableStateFlow(SortType.MARKE)

    // Ruft den Sortiertyp ab und speichert ihn im StateFlow
    private val _fahrzeuge = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.MARKE -> {
                    dao.getFahrzeugeOrderedByMarke()
                }

                SortType.NAME -> dao.getFahrzeugeOrderedByName()
                SortType.PS -> dao.getFahrzeugeOrderedByPS()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(FahrzeugState())
    val state = combine(_state, _sortType, _fahrzeuge) { state, sortType, fahrzeuge ->
        state.copy(
            fahrzeuge = fahrzeuge,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FahrzeugState())

    // Behandelt verschiedene Ereignisse, die das ViewModel beeinflussen
    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: FahrzeugEvent) {
        when (event) {

            // Löschen eines Fahrzeuges Falls das Event eintritt
            is FahrzeugEvent.DeleteFahrzeug -> {
                viewModelScope.launch {
                    try {
                        repository.deleteFahrzeug(event.fahrzeug.id)
                        dao.deleteFahrzeug(event.fahrzeug)
                    } catch (e: Exception) {
                        Log.d("DeleteFahrzeug", "Fahrzeug konnte nicht gelöscht werden!")
                    }

                }
            }

            // Dialogfeld schließen Falls das Event eintritt
            FahrzeugEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingFahrzeug = false
                    )
                }
            }

            // Fahrzeug im Backend speichern Falls dar Event eintritt
            FahrzeugEvent.SaveFahrzeug -> {
                val marke = state.value.marke
                val name = state.value.name
                val ps = state.value.ps
                val preis = state.value.preis
                val standort = state.value.standort
                val ausstattung = state.value.ausstattung
                val zeitraum = state.value.zeitraum
                val fotoURL = state.value.fotoURL

                // Validierung der Eingabe
                if (marke.isBlank() || name.isBlank()) {
                    return
                }

                Log.d("addFahrzeug", "foroURL = ${fotoURL.isEmpty()}")
                //val base64Foto = ConvertPicture().bildUrlToDecodedString(fotoURL)
                val context = getApplication<Application>().applicationContext
                val base64Foto = ConvertPicture().bildUrlToDecodedStringFromActivityResult(
                    uri = fotoURL,
                    context = context
                )
                Log.d("addFahrzeug", "base64Foto = $base64Foto")

                val fahrzeug = Fahrzeug(
                    marke = marke,
                    name = name,
                    ps = ps,
                    preis = preis,
                    standort = standort,
                    ausstattung = ausstattung,
                    zeitraum = zeitraum,
                    fotoURL = fotoURL
                )
                viewModelScope.launch {
                    try {
                        Log.d("addFahrzeug", "Foto is jetzt = ${fahrzeug.fotoURL}")
                        val id = dao.upsertFahrzeug(fahrzeug)

                        Log.d("AddFahrzeug()", "Neue Id = $id")
                        repository.addFahrzeug(fahrzeug.copy(fotoURL = base64Foto, id = id.toInt()))
                    } catch (e: Exception) {
                        Log.d("addFahrzeug()", "Fahrzeug konnte nicht hinzugefügt werden!")
                    }
                }
                _state.update {
                    it.copy(
                        isAddingFahrzeug = false,
                        marke = "",
                        name = "",
                        ps = 0,
                        preis = 0,
                        standort = "",
                        ausstattung = "",
                        zeitraum = "",
                        fotoURL = "",
                    )
                }
            }

            // Marke updaten Falls das Event eintritt
            is FahrzeugEvent.SetMarke -> {
                _state.update {
                    it.copy(
                        marke = event.marke
                    )
                }
            }

            // Name updaten Falls das Event eintritt
            is FahrzeugEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            // Ps updaten Falls das Event eintritt
            is FahrzeugEvent.SetPS -> {
                _state.update {
                    it.copy(
                        ps = event.ps
                    )
                }
            }

            // Preis updaten Falls das Event eintritt
            is FahrzeugEvent.SetPreis -> {
                _state.update {
                    it.copy(
                        preis = event.preis
                    )
                }
            }

            // Standort updaten Falls das Event eintritt
            is FahrzeugEvent.SetStandort -> {
                _state.update {
                    it.copy(
                        standort = event.standort
                    )
                }
            }

            // Ausstattung updaten Falls das Event eintritt
            is FahrzeugEvent.SetAusstattung -> {
                _state.update {
                    it.copy(
                        ausstattung = event.ausstattung
                    )
                }
            }

            // Zeitraum updaten Falls das Event eintritt
            is FahrzeugEvent.SetZeitraum -> {
                _state.update {
                    it.copy(
                        zeitraum = event.zeitraum
                    )
                }
            }

            // BildUrl updaten Falls das Event eintritt
            is FahrzeugEvent.SetFotoURL -> {
                _state.update {
                    it.copy(
                        fotoURL = event.fotoURL
                    )
                }
            }

            // Dialogfeld öffnen Falls das Event eintritt
            FahrzeugEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingFahrzeug = true
                    )
                }
            }

            is FahrzeugEvent.SortFahrzeuge -> {
                _sortType.value = event.sortType
            }
        }
    }
}