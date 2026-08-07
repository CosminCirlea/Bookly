package org.evolutionsoftware.bookly.features.auth.createprofile

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState

val PROFILE_AVATARS =
    listOf("🧒", "👦", "👧", "👶", "🐻", "🦊", "🐰", "🦄", "🐼", "🐯", "🦁", "🐨")

internal data class CreateProfileViewState(
    val name: String = "",
    val dateOfBirth: String = "",
    val isMale: Boolean = true,
    val selectedAvatar: Int = 0,
    val isLoading: Boolean = false,
    val nameError: Boolean = false,
) : ViewState {
    // Birthdate is optional, so only the name gates submission.
    val isFormValid: Boolean
        get() = name.isNotBlank()
}

internal sealed interface CreateProfileSideEffect : SideEffect {
    data object ProfileCreated : CreateProfileSideEffect
    data object Skipped : CreateProfileSideEffect
    data class ShowError(val message: String) : CreateProfileSideEffect
}

internal sealed interface CreateProfileIntent : UserIntent {
    data class NameChanged(val value: String) : CreateProfileIntent
    data class DateOfBirthChanged(val value: String) : CreateProfileIntent
    data class GenderChanged(val isMale: Boolean) : CreateProfileIntent
    data class AvatarSelected(val index: Int) : CreateProfileIntent
    data class Submit(
        val name: String,
        val dateOfBirth: String,
        val isMale: Boolean,
    ) : CreateProfileIntent
    data object Skip : CreateProfileIntent
}

internal sealed interface CreateProfileAction : UserIntentAction {
    data class NameUpdated(val value: String) : CreateProfileAction
    data class DateOfBirthUpdated(val value: String) : CreateProfileAction
    data class GenderUpdated(val isMale: Boolean) : CreateProfileAction
    data class AvatarUpdated(val index: Int) : CreateProfileAction
    data object SubmissionStarted : CreateProfileAction
    data object SubmissionSucceeded : CreateProfileAction
    data class SubmissionFailed(val message: String) : CreateProfileAction
    data object ValidationFailed : CreateProfileAction
    data object Skipped : CreateProfileAction
}
