package com.albanfontaine.gentilvoisin.database

import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UserDbHelper {
    companion object {
        fun getUserCollection() : CollectionReference {
            return FirebaseFirestore.getInstance().collection("users")
        }

        fun getUsers() {
            getUserCollection().get()
        }

        fun getUser(uid: String) {
            getUserCollection().document(uid).get()
        }

        fun createUser(user: User) : Task<Void> {
            return getUserCollection().document(user.uid).set(user)
        }
    }
}