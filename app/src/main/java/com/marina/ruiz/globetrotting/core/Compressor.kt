package com.marina.ruiz.globetrotting.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream

/**
 * Function to compress an image and convert it into a ByteArray. The quality parameter ranges from
 * 0 to 100, where 0 means maximum compression and 100 means minimum compression (no compression).
 * Adjusting maxWidth and maxHeight enables to reduce the dimensions of the image if they exceed
 * the provided values.
 */
fun Uri.compressImage(context: Context, quality: Int, maxWidth: Int = 256, maxHeight: Int = 256): ByteArray? {
    val inputStream = context.contentResolver.openInputStream(this)
    inputStream.use { input ->
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(input, null, this)
            inSampleSize = calculateInSampleSize(this, maxWidth, maxHeight)
            inJustDecodeBounds = false
        }
        val innerInputStream = context.contentResolver.openInputStream(this)
        innerInputStream.use { secondInput ->
            val bitmap = BitmapFactory.decodeStream(secondInput, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            return byteArrayOutputStream.toByteArray()
        }
    }
}

/**
 * Function to compress an image and convert it into a ByteArray
 */
private fun calculateInSampleSize(
    options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}