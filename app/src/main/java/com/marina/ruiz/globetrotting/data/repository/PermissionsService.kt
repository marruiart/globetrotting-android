package com.marina.ruiz.globetrotting.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object PermissionsService {

    private fun openAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.applicationContext.packageName}")
        )
        startActivity(context, intent, null)
    }

    fun requirePermissionsDialog(context: Context) {
        MaterialAlertDialogBuilder(context).setTitle("Faltan permisos")
            .setMessage("No se han dado permisos, ¿desea abrir la configuración de la aplicación para concederlos?")
            .setNeutralButton("No, gracias") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    context, "No se ha podido abrir la cámara.", Toast.LENGTH_SHORT
                ).show()
            }.setPositiveButton("Vale") { dialog, _ ->
                openAppSettings(context)
                dialog.dismiss()
            }.show()
    }
}
