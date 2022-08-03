package com.eratart.domain.repository

import com.eratart.domain.model.domain.DroppUser
import kotlinx.coroutines.flow.Flow

interface IUsersRepository {

    fun getUserByToken(): Flow<DroppUser>

    fun getUserById(userId: Long): Flow<DroppUser>

    fun createUser(user: DroppUser): Flow<DroppUser>

    fun updateUser(userId: Long, user: DroppUser): Flow<DroppUser>

    fun usernameTaken(username: String): Flow<Boolean>
}