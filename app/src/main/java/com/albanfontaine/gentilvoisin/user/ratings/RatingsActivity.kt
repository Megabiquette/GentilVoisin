package com.albanfontaine.gentilvoisin.user.ratings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.view.RatingAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_ratings.*

class RatingsActivity : AppCompatActivity(), RatingsContract.View {
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var ratingList: List<Rating>
    private lateinit var ratedUserUid: String
    private lateinit var ratedUser: User

    private lateinit var presenter: RatingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ratedUserUid = intent.getStringExtra(Constants.USER_UID) ?: ""
        presenter = RatingsPresenter(this, UserRepository, RatingRepository)

        presenter.getRatedUser(ratedUserUid)
        presenter.getRatings(ratedUserUid)

        setContentView(R.layout.activity_ratings)
    }

    override fun onUserRetrieved(user: User) {
        ratedUser = user
    }

    override fun displayRatings(ratings: List<Rating>) {
        ratingList = ratings
        ratingAdapter = RatingAdapter(this, ratingList, UserRepository)
        activityRatingsRecyclerView.apply {
            adapter = ratingAdapter
            layoutManager = LinearLayoutManager(this@RatingsActivity)
        }
        configureView()
    }

    override fun onEmptyRatingList() {
        activityRatingsRecyclerView.isGone = true
    }

    private fun configureView() {
        activityRatingsNumberTextView.text =
            resources.getQuantityString(R.plurals.ratings_number, ratingList.size, ratingList.size, ratedUser.username)
        activityRatingsAddRating.apply {
            text = resources.getString(R.string.ratings_add_rating_button, ratedUser.username)
            setOnClickListener {
                if (ratedUserUid == FirebaseAuth.getInstance().currentUser?.uid) {
                    this@RatingsActivity.toast(R.string.ratings_add_same_user)
                } else {
                    showAddRatingDialog()
                }
            }
        }
    }

    private fun showAddRatingDialog() {
        val dialogFragment = AddRatingDialogFragment()
        val bundle = Bundle().apply {
            putString(Constants.USER_UID, FirebaseAuth.getInstance().currentUser?.uid)
            putString(Constants.RATED_USER_UID, ratedUserUid)
        }
        dialogFragment.arguments = bundle
        dialogFragment.show(supportFragmentManager, "ADD_RATING_DIALOG")
    }
}