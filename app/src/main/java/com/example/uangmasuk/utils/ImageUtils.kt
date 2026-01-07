package com.example.uangmasuk.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Context.copyImageToInternalStorage(uri: Uri): String {
    val inputStream = contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream")

    val fileName = "cashin_${System.currentTimeMillis()}.jpg"
    val file = File(filesDir, fileName)

    FileOutputStream(file).use { output ->
        inputStream.copyTo(output)
    }

    inputStream.close()

    return file.absolutePath
}
