package com.marina.ruiz.globetrotting.core

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import com.google.android.material.appbar.MaterialToolbar
import com.marina.ruiz.globetrotting.R

fun getColorFromThemeAttribute(context: Context, attr: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun setOverflowButtonColor(activity: Activity, color: Int) {
    val toolbar = activity.findViewById<MaterialToolbar>(R.id.mt_main_toolbar)
    val overflowIcon = toolbar.overflowIcon
    overflowIcon?.setTint(color)
}

fun changeStatusBarContrastStyle(window: Window, lightIcons: Boolean) {
    val systemUiVisibility: Int
    val appearance: Int
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        appearance = if (lightIcons) 0 else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        window.decorView.getWindowInsetsController()?.setSystemBarsAppearance(
            appearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        systemUiVisibility = if (lightIcons) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
        window.decorView.systemUiVisibility = systemUiVisibility
    }
}