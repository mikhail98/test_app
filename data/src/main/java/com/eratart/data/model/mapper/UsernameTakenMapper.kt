package com.eratart.data.model.mapper

import com.eratart.data.model.response.UsernameTakenResponse

class UsernameTakenMapper : IMapper<UsernameTakenResponse, Boolean> {

    override fun mapFrom(item: UsernameTakenResponse): Boolean {
        return item.taken ?: true
    }

    override fun mapTo(item: Boolean): UsernameTakenResponse {
        return UsernameTakenResponse(item)
    }
}