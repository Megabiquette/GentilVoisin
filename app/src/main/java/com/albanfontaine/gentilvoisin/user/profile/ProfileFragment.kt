package com.albanfontaine.gentilvoisin.user.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_change_email.view.*
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileContract.View {

    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = ProfilePresenter(this, UserRepository, RatingRepository)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun configureViews(user: User) {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            if (firebaseUser.providerData[1].providerId != "password") {
                profileChangeEmail.isGone = true
                profileChangePassword.isGone = true
            }
        }

        Glide.with(requireContext())
            .load(user.avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_white))
            .into(profileAvatar)

        profileUsername.text = user.username
        profileEmail.text = FirebaseAuth.getInstance().currentUser?.email
        Helper.displayRatingStars(
            requireContext(),
            user.rating,
            ratingStar1,
            ratingStar2,
            ratingStar3,
            ratingStar4,
            ratingStar5
        )

        profileChangeEmail.setOnClickListener {
            showChangeEmailDialog()
        }

        profileChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        profileRatingsOnMe.setOnClickListener {
            val intent = Intent(requireContext(), RatingsActivity::class.java).apply {
                putExtra(Constants.USER_UID, user.uid)
            }
            startActivity(intent)
        }
    }

    private fun showChangeEmailDialog() {
        val layout = requireActivity().layoutInflater.inflate(R.layout.dialog_change_email, null)
        val dialog = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
            .setView(layout)
            .create()

        layout.apply {
            changeEmailDialogValidateButton.setOnClickListener {

            }
            changeEmailDialogCancelButton.setOnClickListener {
                dialog.cancel()
            }
        }

        dialog.show()
    }

    private fun showChangePasswordDialog() {
        val layout = requireActivity().layoutInflater.inflate(R.layout.dialog_change_password, null)
        val dialog = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
            .setView(layout)
            .create()

        layout.apply {
            changePasswordDialogValidateButton.setOnClickListener {

            }
            changePasswordDialogCancelButton.setOnClickListener {
                dialog.cancel()
            }
        }

        dialog.show()
    }
}
