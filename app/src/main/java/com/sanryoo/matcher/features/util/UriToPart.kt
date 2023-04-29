package com.sanryoo.matcher.features.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

/*
*
* Convert uri to part for send file to the server
*
* */

fun uriToPart(context: Context, uri: Uri): MultipartBody.Part {
    val file = uriToFile(context, uri)
    return MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
}

private fun uriToFile(context: Context, uri: Uri): File {
    val filename = getFileName(context, uri)
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, filename)
    file.createNewFile()
    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file
}

private fun getFileName(context: Context, uri: Uri): String {
    var filename = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.let {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            filename = cursor.getString(index)
        }
        it.close()
    }
    return filename
}