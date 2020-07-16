package com.albanfontaine.gentilvoisin.user.ratings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.view.RatingAdapter

class RatingsActivity : AppCompatActivity(), RatingsContract.View {
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var ratings: List<Rating>
    private lateinit var presenter: RatingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter =
            RatingsPresenter(this)

        setContentView(R.layout.activity_ratings)
    }
}