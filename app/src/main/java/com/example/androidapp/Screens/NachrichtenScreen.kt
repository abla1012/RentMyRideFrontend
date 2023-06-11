package com.example.androidapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidapp.R

@Composable
fun NachrichtenScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Nachrichten",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NachrichtenItem(
                iconResId = R.drawable.person_icon,
                name = "Kinen Oilmen",
                message = "Kannst das Auto gern morgen Abend abholen sach ich mal"
            )

            NachrichtenItem(
                iconResId = R.drawable.person_icon,
                name = "Marcell Davis",
                message = "Hallo! Unser neues DSL-Modem könnte Sie interessieren!"
            )
        }
    }
}

@Composable
fun NachrichtenItem(
    iconResId: Int,
    name: String,
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = "Person Icon",
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .align(Alignment.TopStart)
        )

        Text(
            text = name,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp)
        )

        Text(
            text = message,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 60.dp, top = 40.dp)
        )
    }
}