package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object UserRepository {
    private fun getUserCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
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
