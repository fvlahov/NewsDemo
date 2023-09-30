package hr.vlahov.data.models.converters

import hr.vlahov.data.models.database.ProfileEntity
import hr.vlahov.domain.models.profile.Profile


fun Profile.toProfileEntity() = ProfileEntity(name = name)

fun ProfileEntity.toProfile() = Profile(name = name)

fun Collection<ProfileEntity>.toProfiles() = map { it.toProfile() }