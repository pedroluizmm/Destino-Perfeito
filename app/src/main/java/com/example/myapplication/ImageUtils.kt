package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun salvarBitmapNoCache(context: Context, bitmap: Bitmap): String {
        val cacheDir = File(context.cacheDir, "images")
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val file = File(cacheDir, "ponto_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        return file.absolutePath
    }
}