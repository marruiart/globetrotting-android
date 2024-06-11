package com.marina.ruiz.globetrotting.core.extension

import android.util.Patterns

private const val MIN_PASSWORD_LENGTH = 6

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches() || this.isEmpty()

fun String.isValidPassword(): Boolean = this.length >= MIN_PASSWORD_LENGTH || this.isEmpty()

fun validateEmailAndPassword(email: String, password: String): Boolean {
    return email.isValidEmail() && password.isValidPassword()
}

fun String.comparePassword(password: String): Boolean = this == password