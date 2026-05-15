package com.practicum.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel

@Composable
fun NewPlaylistScreen(viewModel: PlaylistsViewModel, onBackClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад") }
            Text("Новый плейлист", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(150.dp).background(Color(0xFFE6E6E6), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.ic_music), contentDescription = null, colorFilter = ColorFilter.tint(Color.Gray), modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Название*") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Описание") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.addPlaylist(name, description)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3772E7))
            ) {
                Text("Создать", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}