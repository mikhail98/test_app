package com.eratart.domain.model.domain

import android.os.Parcelable
import com.eratart.domain.model.enums.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class DroppUser(
    var id: Long?,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var username: String?,
    var photo: String?,
    var description: String?,
    var authId: String?,
    var gender: Gender?,
    var createdAt: String?,
    var updatedAt: String?
): Parcelable