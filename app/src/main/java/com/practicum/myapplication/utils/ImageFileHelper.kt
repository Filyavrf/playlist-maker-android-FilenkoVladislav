package com.practicum.myapplication.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageFileHelper {

    fun copyImageToPrivateStorage(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "playlist_covers")
        if (!file.exists()) file.mkdirs()
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val outputFile = File(file, fileName)
        FileOutputStream(outputFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()
        return outputFile.absolutePath
    }
}