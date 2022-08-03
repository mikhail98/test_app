package com.eratart.data.repository

import com.eratart.data.exception.ThrowableTransformer
import com.eratart.data.model.mapper.UserMapper
import com.eratart.data.model.mapper.UsernameTakenMapper
import com.eratart.data.network.api.UsersApi
import com.eratart.data.repository.base.BaseRepository
import com.eratart.domain.exception.UnauthorizedException
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.domain.repository.IUsersRepository
import com.eratart.tools.auth.IAuthTool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.net.HttpURLConnection

class UsersRepository(
    private val authApi: UsersApi,
    private val authTool: IAuthTool,
) :BaseRepository(), IUsersRepository {

    private val userMapper = UserMapper()

    override fun getUserByToken(): Flow<DroppUser> {
        return getTokenFlow(authTool).flatMapConcat {
            flow { emit(authApi.getUserByToken(it)) }
        }
            .map { userMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }

    override fun getUserById(userId: Long): Flow<DroppUser> {
        return getTokenFlow(authTool).flatMapConcat {
            flow { emit(authApi.getUserById(it, userId)) }
        }
            .map { userMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }

    override fun createUser(user: DroppUser): Flow<DroppUser> {
        return flow { emit(authApi.createUser(userMapper.mapTo(user))) }
            .map { userMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }

    override fun updateUser(userId: Long, user: DroppUser): Flow<DroppUser> {
        return getTokenFlow(authTool).flatMapConcat {
            flow { emit(authApi.updateUser(it, userId, userMapper.mapTo(user))) }
        }
            .map { userMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }

    override fun usernameTaken(username: String): Flow<Boolean> {
        return getTokenFlow(authTool).flatMapConcat {
            flow { emit(authApi.usernameTaken(it, username)) }
        }
            .map { UsernameTakenMapper().mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }
}