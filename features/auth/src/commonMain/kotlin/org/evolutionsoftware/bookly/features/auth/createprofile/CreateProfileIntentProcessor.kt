package org.evolutionsoftware.bookly.features.auth.createprofile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.CreateProfileUseCase

internal class CreateProfileIntentProcessor(
    private val createProfileUseCase: CreateProfileUseCase,
) : IntentProcessor<CreateProfileIntent, CreateProfileAction> {
    override fun invoke(intent: CreateProfileIntent): Flow<CreateProfileAction> =
        when (intent) {
            is CreateProfileIntent.NameChanged -> flowOf(CreateProfileAction.NameUpdated(intent.value))
            is CreateProfileIntent.DateOfBirthChanged -> flowOf(CreateProfileAction.DateOfBirthUpdated(intent.value))
            is CreateProfileIntent.GenderChanged -> flowOf(CreateProfileAction.GenderUpdated(intent.isMale))
            is CreateProfileIntent.AvatarSelected -> flowOf(CreateProfileAction.AvatarUpdated(intent.index))
            CreateProfileIntent.Skip -> flowOf(CreateProfileAction.Skipped)
            is CreateProfileIntent.Submit ->
                flow {
                    if (intent.name.isBlank()) {
                        emit(CreateProfileAction.ValidationFailed)
                        return@flow
                    }
                    emit(CreateProfileAction.SubmissionStarted)
                    when (val result = createProfileUseCase(intent.name.trim(), intent.dateOfBirth, intent.isMale)) {
                        is Result.Success -> emit(CreateProfileAction.SubmissionSucceeded)
                        is Result.Error -> emit(CreateProfileAction.SubmissionFailed(result.error.toString()))
                    }
                }
        }
}
