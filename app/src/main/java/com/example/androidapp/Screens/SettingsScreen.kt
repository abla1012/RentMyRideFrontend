package com.example.androidapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidapp.Room.FahrzeugEvent
import com.example.androidapp.Room.FahrzeugState

@Composable
fun SettingsScreen(
    state: FahrzeugState,
    onEvent: (FahrzeugEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Einstellungen",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        SettingsItem(text = "Bestellhistorie")
        SettingsItem(text = "Zahlungsmethoden")
        SettingsItem(text = "Kontoinformationen")
        SettingsItem(text = "Datenschutz")
        SettingsItem(text = "Barrierefreiheit")
        SettingsItem(text = "App-Einstellungen")
//        Spacer(modifier = Modifier.weight(1f))
        SettingsItem(
            text = "Konto löschen",
            backgroundColor = Color.Red,
        )
    }
}

@Composable
fun SettingsItem(
    text: String,
    backgroundColor: Color = Color(0xFF4B4B4B),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(60.dp)
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}