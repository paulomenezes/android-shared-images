package com.paulomenezes.sharedstorage.utils

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.paulomenezes.sharedstorage.MainActivity
import com.paulomenezes.sharedstorage.models.FileImage

class ContentProviderUtils(private val context: AppCompatActivity) {
    fun loadImages(): MutableList<FileImage> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.RELATIVE_PATH
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val imageList = mutableListOf<FileImage>()

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            // val relativePathColumn = cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                // val relativePath = cursor.getString(relativePathColumn)

                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageList.add(FileImage(displayName, imageUri))
            }

            // binding.imageView.setImageURI(imageUri)
        }

        return imageList
    }

    fun deleteImage(uri: Uri) {
        try {
            context.contentResolver.delete(
                uri,
                "${MediaStore.Images.Media._ID} = ?",
                arrayOf(ContentUris.parseId(uri).toString())
            )
        } catch (exception: SecurityException) {
            val recoverySecurityException = exception as? RecoverableSecurityException ?: throw exception
            val intentSender = recoverySecurityException.userAction.actionIntent.intentSender

            val intent = Intent()
            intent.putExtra(MainActivity.INTENT_EXTRA_DELETE, uri)

            context.startIntentSenderForResult(intentSender, MainActivity.REQUEST_DELETE, intent, 0, 0, 0, null)
        }
    }
}