package org.evolutionsoftware.bookly.services.auth.data.mapper

import org.evolutionsoftware.bookly.services.auth.data.dto.AuthResponseDto
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthUser

internal fun AuthResponseDto.toDomain(): AuthSession =
    AuthSession(
        user = AuthUser(
            id = user.id,
            email = user.email,
            role = user.role,
        ),
        accessToken = session.accessToken,
        refreshToken = session.refreshToken,
        expiresIn = session.expiresIn,
    )
