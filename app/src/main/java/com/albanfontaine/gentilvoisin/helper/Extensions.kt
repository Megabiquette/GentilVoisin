package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class Extensions {
    companion object {
        fun Context.toast(@StringRes intRes: Int) {
            Toast.makeText(this, intRes, Toast.LENGTH_LONG).show()
        }
    }
}