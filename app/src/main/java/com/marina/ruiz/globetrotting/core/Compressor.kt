package com.marina.ruiz.globetrotting.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Function to compress an image and convert it into a ByteArray. The quality parameter ranges from
 * 0 to 100, where 0 means maximum compression and 100 means minimum compression (no compression).
 * Adjusting maxWidth and maxHeight enables to reduce the dimensions of the image if they exceed
 * the provided values.
 */
fun Uri.compressAndResizeImage(
    context: Context, quality: Int, maxWidth: Int = 256, maxHeight: Int = 256
): ByteArray? {
    val optionsInputStream = context.contentResolver.openInputStream(this)

    optionsInputStream.use { optionsInput ->
        val options = getOptions(optionsInput, maxWidth, maxHeight)
        val rotateInputStream = context.contentResolver.openInputStream(this)

        rotateInputStream.use { rotationInput ->
            val matrix = getRotation(rotationInput)
            val bitmapInputStream = context.contentResolver.openInputStream(this)

            bitmapInputStream.use { bitmapInput ->
                val bitmap = BitmapFactory.decodeStream(bitmapInput, null, options)
                val rotatedBitmap = rotateBitmap(bitmap, matrix)
                val byteArrayOutputStream = ByteArrayOutputStream()

                rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }
        }
    }
}

private fun rotateBitmap(bitmap: Bitmap?, matrix: Matrix): Bitmap? = bitmap?.let {
    Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private fun getOptions(input: InputStream?, maxWidth: Int, maxHeight: Int): BitmapFactory.Options {
    return BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeStream(input, null, this)
        inSampleSize = calculateInSampleSize(this, maxWidth, maxHeight)
        inJustDecodeBounds = false
    }
}

private fun getRotation(input: InputStream?): Matrix {
    val exifInterface = input?.let { ExifInterface(it) }
    val orientation = exifInterface?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }
    return matrix
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