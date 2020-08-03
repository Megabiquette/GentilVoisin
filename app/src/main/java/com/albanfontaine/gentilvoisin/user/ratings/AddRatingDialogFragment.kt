package com.albanfontaine.gentilvoisin.user.ratings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import kotlinx.android.synthetic.main.dialog_add_rating.view.*

class AddRatingDialogFragment : DialogFragment() {

    private lateinit var starList: MutableList<ImageView>
    private var ratingScore = 0
    private var comment = ""
    private lateinit var userUid: String
    private lateinit var ratedUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            userUid = bundle.getString(Constants.USER_UID, "")
            ratedUserUid = bundle.getString(Constants.RATED_USER_UID, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val layout = requireActivity().layoutInflater.inflate(R.layout.dialog_add_rating, null)
            configurateRatingStars(layout)

            val builder = AlertDialog.Builder(it, R.style.DialogTheme)
            builder
                .setView(layout)
                .setPositiveButton(R.string.ratings_add_ratings_dialog_positive_button) { _, _ ->
                    comment = layout.dialogAddRatingCommentInput.text.toString().trim()
                    onPositiveButtonClicked()
                }
                .setNegativeButton(R.string.ratings_add_ratings_dialog_negative_button) { _, _ ->
                    dialog?.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun configurateRatingStars(view: View) {
        starList = mutableListOf()
        starList.add(view.ratingStar1)
        starList.add(view.ratingStar2)
        starList.add(view.ratingStar3)
        starList.add(view.ratingStar4)
        starList.add(view.ratingStar5)

        for (i in 0 until starList.size) {
            starList[i].setOnClickListener {
                onStarClicked(i)
            }
        }
    }

    private fun onStarClicked(starPosition: Int) {
        ratingScore = starPosition + 1

        for (i in 0 until starList.size) {
            if ( i <= starPosition) {
                starList[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star))
            } else {
                starList[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_border))
            }
        }
    }

    private fun onPositiveButtonClicked() {
        if (ratingScore == 0) {
            requireContext().toast(R.string.ratings_add_rating_dialog_no_star_clicked)
        } else {
            val rating = Rating(
                posterUid = userUid,
                userRatedUid = ratedUserUid,
                score = ratingScore,
                comment = comment
            )
            RatingRepository.createRating(rating)
            requireActivity().finish()
        }
    }
}