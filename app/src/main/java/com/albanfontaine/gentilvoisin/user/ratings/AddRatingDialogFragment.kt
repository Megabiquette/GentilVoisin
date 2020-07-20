package com.albanfontaine.gentilvoisin.user.ratings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.albanfontaine.gentilvoisin.R

class AddRatingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.DialogTheme)
            val inflater = requireActivity().layoutInflater
            builder
                .setView(inflater.inflate(R.layout.dialog_add_rating, null))
                .setPositiveButton(R.string.ratings_add_ratings_dialog_positive_button) { _, _ ->
                    // TODO
                }
                .setNegativeButton(R.string.ratings_add_ratings_dialog_negative_button) { _, _ ->
                    dialog?.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}