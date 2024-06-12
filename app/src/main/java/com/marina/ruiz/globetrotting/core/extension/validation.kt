package com.marina.ruiz.globetrotting.core.extension

import android.util.Patterns

private const val MIN_PASSWORD_LENGTH = 6

/**
 * Validates the email format.
 *
 * @return True if the email is valid or empty, false otherwise.
 */
fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches() || this.isEmpty()

/**
 * Validates the password length.
 *
 * @return True if the password length is valid or empty, false otherwise.
 */
fun String.isValidPassword(): Boolean = this.length >= MIN_PASSWORD_LENGTH || this.isEmpty()

/**
 * Validates both email and password.
 *
 * @param email The email to be validated.
 * @param password The password to be validated.
 * @return True if both the email and password are valid, false otherwise.
 */
fun validateEmailAndPassword(email: String, password: String): Boolean {
    return email.isValidEmail() && password.isValidPassword()
}

/**
 * Compares two passwords for equality.
 *
 * @param password The password to compare with.
 * @return True if the passwords are equal, false otherwise.
 */
fun String.comparePassword(password: String): Boolean = this == password