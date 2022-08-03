package com.eratart.domain.interactor.users

import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.domain.Item
import com.eratart.domain.model.domain.Look
import kotlinx.coroutines.flow.Flow

interface IUsersInteractor {
    fun getUserByToken(): Flow<DroppUser>
    fun getUserById(userId: Long): Flow<DroppUser>
    fun createUser(user: DroppUser): Flow<DroppUser>
    fun updateUser(userId: Long, user: DroppUser): Flow<DroppUser>
    fun usernameTaken(username: String): Flow<Boolean>
    fun getItemsList(userId: Long): Flow<List<Item>>
    fun getLooksList(userId: Long): Flow<List<Look>>
    fun getCapsulesList(userId: Long): Flow<List<Capsule>>
    fun fetchAdverts(userId: Long): Flow<List<Advert>>
    fun fetchMyAdverts(userId: Long): Flow<List<Advert>>
    fun fetchFavoriteAdverts(userId: Long): Flow<List<Advert>>
}