package com.practicum.myapplication.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R

@Composable
fun SettingsScreen(onBackClick: () -> Boolean) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        SettingItem(
            text = stringResource(R.string.share_app),
            icon = Icons.Default.Share,
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
                }
                context.startActivity(Intent.createChooser(intent, null))
            }
        )

        SettingItem(
            text = stringResource(R.string.write_to_support),
            icon = Icons.Default.Email,
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("vlad.filenko98@mail.ru"))
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_body))
                }
                context.startActivity(intent)
            }
        )

        SettingItem(
            text = stringResource(R.string.user_agreement),
            icon = Icons.Default.ArrowForward,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.agreement_link)))
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun SettingItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = Color.Black
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}