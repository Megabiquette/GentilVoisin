package com.albanfontaine.gentilvoisin.user.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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

        }

        profileChangePassword.setOnClickListener {

        }

        profileRatingsOnMe.setOnClickListener {
            val intent = Intent(requireContext(), RatingsActivity::class.java).apply {
                putExtra(Constants.USER_UID, user.uid)
            }
            startActivity(intent)
        }
    }
}
