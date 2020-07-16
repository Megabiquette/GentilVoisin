package com.albanfontaine.gentilvoisin.user.ratings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.view.RatingAdapter
import kotlinx.android.synthetic.main.activity_ratings.*

class RatingsActivity : AppCompatActivity(), RatingsContract.View {
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var ratingList: List<Rating>
    private lateinit var ratedUserUid: String

    private lateinit var presenter: RatingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ratedUserUid = intent.getStringExtra(Constants.USER_UID) ?: ""
        presenter = RatingsPresenter(this, RatingRepository)

        presenter.getRatings(ratedUserUid)

        setContentView(R.layout.activity_ratings)
    }

    override fun displayRatings(ratings: List<Rating>) {
        ratingList = ratings
        ratingAdapter = RatingAdapter(this, ratingList, UserRepository)
        activityRatingsRecyclerView.apply {
            adapter = ratingAdapter
            layoutManager = LinearLayoutManager(this@RatingsActivity)
        }
    }

    override fun onEmptyRatingList() {
        activityRatingsRecyclerView.isGone = true
    }
}