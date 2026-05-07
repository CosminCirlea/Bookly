package org.evolutionsoftware.bookly.services.profiles.data.mapper

import org.evolutionsoftware.bookly.services.profiles.data.dto.ProfileDto
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

internal fun ProfileDto.toDomain(): ParentProfile =
    ParentProfile(id = id.toString(), displayName = name)
