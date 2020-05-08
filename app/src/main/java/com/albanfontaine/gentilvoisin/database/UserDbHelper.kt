package com.albanfontaine.gentilvoisin.database

import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object UserDbHelper {
    private fun getUserCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection("users")
    }

    fun getUsers() : Task<QuerySnapshot> {
        return getUserCollection().get()
    }

    fun getUser(uid: String) : Task<DocumentSnapshot> {
        return getUserCollection()
            .document(uid)
            .get()
    }

    fun createUser(user: User) : Task<Void> {
        return getUserCollection()
            .document(user.uid)
            .set(user)
    }
}
