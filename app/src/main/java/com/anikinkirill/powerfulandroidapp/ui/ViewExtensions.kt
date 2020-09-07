package com.anikinkirill.powerfulandroidapp.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.anikinkirill.powerfulandroidapp.R

fun Context.displayToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.displaySuccessDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            positiveButton(R.string.text_ok)
            message(text = message)
        }
}

fun Context.displayErrorDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            positiveButton(R.string.text_ok)
            message(text = message)
        }
}

