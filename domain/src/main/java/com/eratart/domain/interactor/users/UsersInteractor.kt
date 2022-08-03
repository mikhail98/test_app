package com.eratart.domain.interactor.users

import com.eratart.core.coroutines.asFlow
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.domain.Item
import com.eratart.domain.model.domain.Look
import com.eratart.domain.repository.IUsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class UsersInteractor(private val usersRepository: IUsersRepository) :
    IUsersInteractor {

    override fun getUserByToken(): Flow<DroppUser> {
        return usersRepository.getUserByToken().flowOn(Dispatchers.IO)
    }

    override fun getUserById(userId: Long): Flow<DroppUser> {
        return usersRepository.getUserById(userId).flowOn(Dispatchers.IO)
    }

    override fun createUser(user: DroppUser): Flow<DroppUser> {
        return usersRepository.createUser(user).flowOn(Dispatchers.IO)
    }

    override fun updateUser(userId: Long, user: DroppUser): Flow<DroppUser> {
        return usersRepository.updateUser(userId, user).flowOn(Dispatchers.IO)
    }

    override fun usernameTaken(username: String): Flow<Boolean> {
        return usersRepository.usernameTaken(username).flowOn(Dispatchers.IO)
    }

    override fun getItemsList(userId: Long): Flow<List<Item>> {
        return MockUtil.getItemsList().asFlow()
    }

    override fun getLooksList(userId: Long): Flow<List<Look>> {
        return MockUtil.getLooksList().asFlow()
    }

    override fun getCapsulesList(userId: Long): Flow<List<Capsule>> {
        return MockUtil.getCapsulesList().asFlow()
    }

    override fun fetchAdverts(userId: Long): Flow<List<Advert>> {
        return MockUtil.getAdvertsList().asFlow()
    }

    override fun fetchFavoriteAdverts(userId: Long): Flow<List<Advert>> {
        return MockUtil.getFavoriteAdvertsList().asFlow()
    }

    override fun fetchMyAdverts(userId: Long): Flow<List<Advert>> {
        return MockUtil.getMyAdvertsList().asFlow()
    }
}