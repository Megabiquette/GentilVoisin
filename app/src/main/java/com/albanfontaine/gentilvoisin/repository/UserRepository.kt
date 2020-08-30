package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object UserRepository {

    private fun getUserCollection(): CollectionReference
            = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)

    fun getCurrentUser(callback: FirebaseUserCallback) {
        getUser(FirebaseAuth.getInstance().currentUser!!.uid, callback)
    }

    fun getUser(uid: String, callback: FirebaseUserCallback) {
        getUserCollection()
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)!!
                    callback.onUserRetrieved(user)
                }
            }
    }

    fun isNewUser(callback: FirebaseUserCallback) {
        var isNew: Boolean = false
        getUserCollection()
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnCompleteListener { document ->
                if (document.result!!.getString("username") == null) {
                    isNew = true
                }
                callback.isNewUser(isNew)
            }
    }

    fun createUser(user: User): Task<Void> {
        return getUserCollection()
            .document(user.uid)
            .set(user)
    }

    fun updateUserCity(user: User, city: String): Task<Void> {
        return getUserCollection()
            .document(user.uid)
            .update(Constants.DB_FIELD_CITY, city)
    }
}
