package com.marina.ruiz.globetrotting.core.extension

import android.widget.EditText

fun EditText.loseFocusAfterAction(action: Int) {
    this.setOnEditorActionListener { v, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            action -> {
                this.dismissKeyboard()
                v.clearFocus()
                true
            }
            else -> false
        }
    }
}
