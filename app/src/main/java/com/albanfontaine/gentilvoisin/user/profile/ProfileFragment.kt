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
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosActivity
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.dialog_change_email.view.*
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private val userRepository = UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlobalScope.launch {
            val currentUser = userRepository.getCurrentUser()

            withContext(Dispatchers.Main) {
                configureViews(currentUser)
            }
        }
    }

    private fun configureViews(user: User) {
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
        profileCity.text = user.city
        Helper.displayRatingStars(
            requireContext(),
            user,
            ratingStar1,
            ratingStar2,
            ratingStar3,
            ratingStar4,
            ratingStar5,
            profileNotEnoughRating,
            this.getString(R.string.profile_not_enough_ratings),
            null
        )

        profileChangeEmail.setOnClickListener {
            showChangeEmailDialog()
        }

        profileChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        profileChangeCity.setOnClickListener {
            val intent = Intent(requireContext(), RegisterInfosActivity::class.java).apply {
                putExtra(Constants.CHANGE_CITY_FOR_EXISTING_USER, true)
            }
            startActivity(intent)
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
                FirebaseAuth.getInstance().currentUser?.run {
                    val newMail = changeEmailDialogEditText.text.trim().toString()
                    updateEmail(newMail)
                        .addOnSuccessListener {
                            profileEmail.text = newMail
                            requireActivity().toast(R.string.profile_change_email_success)
                            dialog.cancel()
                        }
                        .addOnFailureListener { exception ->
                            handleChangeException(exception, dialog)
                        }
                }
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
                if (changePasswordDialogEditText.text.trim().toString() != confirmPasswordDialogEditText.text.trim().toString()) {
                    requireActivity().toast(R.string.profile_change_password_error_same_password)
                    dialog.cancel()
                } else {
                    FirebaseAuth.getInstance().currentUser?.run {
                        updatePassword(changePasswordDialogEditText.text.trim().toString())
                            .addOnSuccessListener {
                                requireActivity().toast(R.string.profile_change_password_success)
                                dialog.cancel()
                            }
                            .addOnFailureListener { exception ->
                                handleChangeException(exception, dialog, true)
                            }
                    }
                }
            }
            changePasswordDialogCancelButton.setOnClickListener {
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun handleChangeException(exception: Exception, dialog: AlertDialog, isPassword: Boolean = false) {
        when (exception) {
            is FirebaseAuthInvalidCredentialsException ->
                if (isPassword) {
                    requireActivity().toast(R.string.profile_change_password_error_weak_password)
                } else {
                    requireActivity().toast(R.string.profile_change_email_error_email_malformatted)
                }
            is FirebaseAuthRecentLoginRequiredException ->
                if (isPassword) {
                    requireActivity().toast(R.string.profile_change_password_error_login_required)
                } else {
                    requireActivity().toast(R.string.profile_change_email_error_login_required)
                }
            is FirebaseAuthUserCollisionException ->
                requireActivity().toast(R.string.profile_change_email_error_email_already_exists)
            else -> requireActivity().toast(R.string.profile_change_email_error_other)
        }
        dialog.cancel()
    }
}
