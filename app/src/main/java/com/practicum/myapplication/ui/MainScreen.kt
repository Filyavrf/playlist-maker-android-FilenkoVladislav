package com.practicum.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R

@Composable
fun MainScreen(onSearchClick: () -> Unit, onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF3772E7)).padding(20.dp)) {
            Text(text = stringResource(R.string.header_main), color = Color.White, style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.height(24.dp))
        MenuRow(text = stringResource(R.string.search), icon = Icons.Default.Search, onClick = onSearchClick)
        MenuRow(text = stringResource(R.string.settings), icon = Icons.Default.Settings, onClick = onSettingsClick)
    }
}

@Composable
fun MenuRow(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = Color.Black)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 18.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}