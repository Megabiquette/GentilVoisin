package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.User
import com.google.android.gms.tasks.Task

interface UserRepositoryInterface {

    suspend fun getCurrentUser(): User
    suspend fun getUser(uid: String): User
    suspend fun isNewUser(): Boolean
    fun createUser(user: User): Task<Void>
    fun updateUserCity(user: User, city: String): Task<Void>
}