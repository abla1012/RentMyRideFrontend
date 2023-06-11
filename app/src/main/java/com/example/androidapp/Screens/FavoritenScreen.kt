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
fun FavoritenScreen(
    state: FahrzeugState,
    onEvent: (FahrzeugEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Deine Favoritenliste ist leer!",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 64.dp)
        )

        Text(
            text = "Registriere dich jetzt und erhalte Zugriff auf folgende Vorteile:",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            modifier = Modifier.padding(top = 16.dp, start = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "• Schneller Zugriff auf deine Lieblingsautos",
                color = Color.White,
                fontSize = 16.sp
            )

            Text(
                text = "• Bequemer Vergleich verschiedener Anbieter",
                color = Color.White,
                fontSize = 16.sp
            )

            Text(
                text = "• Attraktive Angebote für später merken",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(45.dp)
                .background(Color(0xFF8800FF), shape = RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "Registrieren",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(45.dp)
                .background(Color(0xFF8800FF), shape = RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "Anmelden",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}