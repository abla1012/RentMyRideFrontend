package com.example.androidapp.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidapp.Room.FahrzeugEvent
import com.example.androidapp.Room.FahrzeugState

/**
Composable-Funktion, die die Hauptansicht für die Fahrzeugerstellung rendert.
@param state Der Zustand der Fahrzeugerstellung, der die aktuellen Werte der Fahrzeugattribute und den Zustand der Dialogansicht enthält.
@param onEvent Eine Callback-Funktion, die aufgerufen wird, wenn ein Event in der Hauptansicht ausgelöst wird.
 */
@Composable
fun CreateScreen(
    state: FahrzeugState,
    onEvent: (FahrzeugEvent) -> Unit
) {
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(FahrzeugEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Fahrzeug"
                )
            }
        },
    ) { _ ->
        if(state.isAddingFahrzeug) {
            AddFahrzeugDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.fahrzeuge) { fahrzeug ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${fahrzeug.marke} ${fahrzeug.name}",
                            fontSize = 20.sp
                        )
                        Text(text = fahrzeug.ps.toString(), fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        onEvent(FahrzeugEvent.DeleteFahrzeug(fahrzeug))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Fahrzeug"
                        )
                    }
                }
            }
        }
    }
}