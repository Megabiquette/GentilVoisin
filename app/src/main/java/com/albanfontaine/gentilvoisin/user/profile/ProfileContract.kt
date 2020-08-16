package com.albanfontaine.gentilvoisin.user.profile

import com.albanfontaine.gentilvoisin.model.User


interface ProfileContract {
    interface View {
        fun configureViews(user: User)
    }

    interface Presenter {
    }
}