package com.example.streamtv.Utils

import android.util.Patterns
import android.widget.EditText

object Validator {

    private const val MIN_PASSWD_LENGTH = 6

    fun checkEmpty(editText: EditText): Boolean {
        val value = getEditTextValue(editText)
        if (value.isEmpty()) {
            editText.error = "This field is required"
            editText.requestFocus()
            return false
        }
        return true
    }

    fun checkEmail(emailInput: EditText): Boolean {
        val email = getEditTextValue(emailInput)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Provide valid email address"
            emailInput.requestFocus()
            return false
        }
        return true
    }

    fun checkPasswdLength(passwordInput: EditText): Boolean {
        val password = getEditTextValue(passwordInput)
        if (password.length < MIN_PASSWD_LENGTH) {
            passwordInput.error = "Min password length is 6"
            passwordInput.requestFocus()
            return false
        }
        return true
    }

    private fun getEditTextValue(editText: EditText): String {
        return editText.text.toString().trim()
    }
}
