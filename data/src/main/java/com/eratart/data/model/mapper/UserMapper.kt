package com.eratart.data.model.mapper

import com.eratart.data.model.response.UserResponse
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.enums.Gender

class UserMapper : IMapper<UserResponse, DroppUser> {

    private val genderMapper by lazy { GenderMapper() }

    override fun mapFrom(item: UserResponse): DroppUser {
        return with(item) {
            val gender = genderMapper.mapFrom(gender)
            DroppUser(
                _id,
                firstName,
                lastName,
                email,
                username,
                photo,
                description,
                authId,
                gender,
                createdAt.orEmpty(),
                updatedAt.orEmpty()
            )
        }
    }

    override fun mapTo(item: DroppUser): UserResponse {
        return with(item) {
            val gender = genderMapper.mapTo(gender ?: Gender.UNISEX)
            UserResponse(
                id,
                firstName,
                lastName,
                email,
                username,
                photo,
                description,
                authId,
                gender,
                createdAt,
                updatedAt
            )
        }
    }
}