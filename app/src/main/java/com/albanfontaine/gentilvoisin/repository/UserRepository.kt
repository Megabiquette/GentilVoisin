package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserRepository {

    private fun getUserCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)

    suspend fun getCurrentUser(): User {
        return getUser(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    suspend fun getUser(uid: String): User {
        var user: User? = null
        getUserCollection()
            .document(uid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    user = task.result!!.toObject(User::class.java)
                }
            }
            .await()
        return user!!
    }

    suspend fun isNewUser(): Boolean {
        return getUserCollection()
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    return@continueWith task.result!!.getString("username") == null
                }
                return@continueWith false
            }
            .await()
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
