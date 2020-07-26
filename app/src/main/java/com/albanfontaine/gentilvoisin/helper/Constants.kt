package com.albanfontaine.gentilvoisin.helper

object Constants {
    // Identifier for Sign in activity
    const val RC_SIGN_IN = 100

    // Firestore Collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_JOBS = "jobs"
    const val COLLECTION_RATINGS = "ratings"
    const val COLLECTION_MESSAGES = "messages"

    // Firestore Fields
    const val DB_FIELD_POSTER_UID = "posterUid"
    const val DB_FIELD_CITY = "city"
    const val DB_FIELD_CATEGORY = "category"
    const val DB_FIELD_TYPE = "type"
    const val DB_FIELD_DONE = "done"
    const val DB_FIELD_POSTED_AT = "postedAt"
    const val DB_FIELD_USER_RATED_UID = "userRatedUid"
    const val DB_FIELD_SENDER_UID = "senderUid"
    const val DB_FIELD_RECIPIENT_UID_UID = "recipientUid"

    // Bundle
    const val JOB_UID = "JOB_UID"
    const val USER_UID = "USER_UID"
    const val RATED_USER_UID = "RATED_USER_UID"
}