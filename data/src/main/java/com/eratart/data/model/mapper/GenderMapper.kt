package com.eratart.data.model.mapper

import com.eratart.domain.model.enums.Gender

class GenderMapper : IMapper<Int?, Gender> {

    override fun mapFrom(item: Int?): Gender {
        return when (item) {
            0 -> Gender.WOMAN
            1 -> Gender.MAN
            else -> Gender.UNISEX
        }
    }

    override fun mapTo(item: Gender): Int? {
        return when (item) {
            Gender.WOMAN -> 0
            Gender.MAN -> 1
            Gender.UNISEX -> null
        }
    }
}