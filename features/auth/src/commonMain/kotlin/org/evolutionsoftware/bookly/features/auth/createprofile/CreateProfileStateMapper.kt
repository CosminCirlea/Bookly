package org.evolutionsoftware.bookly.features.auth.createprofile

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class CreateProfileStateMapper : StateMapper<CreateProfileAction, CreateProfileViewState> {
    override fun invoke(
        action: CreateProfileAction,
        currentState: CreateProfileViewState,
    ): CreateProfileViewState =
        when (action) {
            is CreateProfileAction.NameUpdated ->
                currentState.copy(name = action.value, nameError = false)
            is CreateProfileAction.DateOfBirthUpdated ->
                currentState.copy(dateOfBirth = action.value)
            is CreateProfileAction.GenderUpdated ->
                currentState.copy(isMale = action.isMale)
            is CreateProfileAction.AvatarUpdated ->
                currentState.copy(selectedAvatar = action.index)
            CreateProfileAction.SubmissionStarted ->
                currentState.copy(isLoading = true, nameError = false)
            CreateProfileAction.SubmissionSucceeded ->
                currentState.copy(isLoading = false)
            is CreateProfileAction.SubmissionFailed ->
                currentState.copy(isLoading = false)
            CreateProfileAction.ValidationFailed ->
                currentState.copy(isLoading = false, nameError = true)
            CreateProfileAction.Skipped -> currentState
        }
}
