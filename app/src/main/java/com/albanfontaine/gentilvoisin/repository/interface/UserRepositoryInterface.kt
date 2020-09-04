package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.User

interface UserRepositoryInterface {

    suspend fun getCurrentUser(): User
    suspend fun getUser(uid: String): User
    suspend fun isNewUser(): Boolean
    suspend fun createUser(user: User): Boolean
    suspend fun updateUserCity(user: User, city: String): Boolean
}