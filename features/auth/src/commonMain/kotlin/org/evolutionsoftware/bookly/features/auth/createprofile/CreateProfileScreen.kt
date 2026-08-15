package org.evolutionsoftware.bookly.features.auth.createprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_create_profile_avatar_label
import bookly.features.auth.generated.resources.auth_create_profile_dob_label
import bookly.features.auth.generated.resources.auth_create_profile_dob_placeholder
import bookly.features.auth.generated.resources.auth_create_profile_gender_boy
import bookly.features.auth.generated.resources.auth_create_profile_gender_girl
import bookly.features.auth.generated.resources.auth_create_profile_gender_label
import bookly.features.auth.generated.resources.auth_create_profile_name_error
import bookly.features.auth.generated.resources.auth_create_profile_name_label
import bookly.features.auth.generated.resources.auth_create_profile_name_placeholder
import bookly.features.auth.generated.resources.auth_create_profile_skip
import bookly.features.auth.generated.resources.auth_create_profile_submit
import bookly.features.auth.generated.resources.auth_create_profile_subtitle
import bookly.features.auth.generated.resources.auth_create_profile_title
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.getString

private val AVATAR_BACKGROUNDS = listOf(
    Color(0xFFFFE796),
    Color(0xFFD4F0FF),
    Color(0xFFFFD4D4),
    Color(0xFFD4FFE0),
    Color(0xFFE8D4FF),
    Color(0xFFFFEDD4),
    Color(0xFFD4EEFF),
    Color(0xFFFFF0D4),
    Color(0xFFD4FFD4),
    Color(0xFFFFD4F5),
    Color(0xFFDCE7FF),
    Color(0xFFFFE3C7),
)

@Composable
fun CreateProfileRoute(
    onProfileCreated: () -> Unit,
    onSkip: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberCreateProfileViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                CreateProfileSideEffect.ProfileCreated -> onProfileCreated()
                CreateProfileSideEffect.Skipped -> onSkip()
                is CreateProfileSideEffect.ShowError -> onShowMessage(getString(effect.message))
            }
        }
    }

    CreateProfileContent(
        viewState = viewState,
        onIntent = viewModel::onUserIntent,
    )
}

@Composable
internal fun CreateProfileContent(
    viewState: CreateProfileViewState,
    onIntent: (CreateProfileIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            containerColor = TokenProvider.colors.bgBase,
            topBar = {
                Header(
                    properties =
                        HeaderProperties(
                            title = stringResource(Res.string.auth_create_profile_title),
                        ),
                    onLeadingClick = { onIntent(CreateProfileIntent.Skip) },
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .padding(horizontal = TokenProvider.spacings.horizontalSpacing)
                        .padding(bottom = TokenProvider.spacings.screenBottomSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.auth_create_profile_subtitle),
                    style = TokenProvider.textStyles.body,
                    color = TokenProvider.colors.textMuted,
                    modifier = Modifier.padding(top = TokenProvider.spacings.formGapSm),
                )

                Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))

                // Avatar picker
                SectionLabel(stringResource(Res.string.auth_create_profile_avatar_label))
                Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
                AvatarGrid(
                    selected = viewState.selectedAvatar,
                    onSelect = { onIntent(CreateProfileIntent.AvatarSelected(it)) },
                )

                Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))

                // Name field
                TextField(
                    properties =
                        TextFieldProperties(
                            label = stringResource(Res.string.auth_create_profile_name_label),
                            placeholder = stringResource(Res.string.auth_create_profile_name_placeholder),
                            state =
                                when {
                                    viewState.isLoading -> TextFieldProperties.State.Disabled
                                    viewState.nameError -> TextFieldProperties.State.Error
                                    else -> TextFieldProperties.State.Default
                                },
                        ),
                    value = viewState.name,
                    onValueChange = { onIntent(CreateProfileIntent.NameChanged(it)) },
                    enabled = !viewState.isLoading,
                )
                if (viewState.nameError) {
                    Text(
                        text = stringResource(Res.string.auth_create_profile_name_error),
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = TokenProvider.spacings.xxs),
                        style = TokenProvider.textStyles.body,
                        color = TokenProvider.colors.textDanger,
                    )
                }

                Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))

                // Date of birth
                TextField(
                    properties =
                        TextFieldProperties(
                            label = stringResource(Res.string.auth_create_profile_dob_label),
                            placeholder = stringResource(Res.string.auth_create_profile_dob_placeholder),
                            state =
                                if (viewState.isLoading) {
                                    TextFieldProperties.State.Disabled
                                } else {
                                    TextFieldProperties.State.Default
                                },
                        ),
                    value = viewState.dateOfBirth,
                    onValueChange = { onIntent(CreateProfileIntent.DateOfBirthChanged(it)) },
                    enabled = !viewState.isLoading,
                )

                Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))

                // Gender selector
                SectionLabel(stringResource(Res.string.auth_create_profile_gender_label))
                Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapSm))
                GenderSelector(
                    isMale = viewState.isMale,
                    enabled = !viewState.isLoading,
                    onSelect = { onIntent(CreateProfileIntent.GenderChanged(it)) },
                )

                Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))

                // Submit
                Button(
                    properties =
                        ButtonProperties(
                            label = stringResource(Res.string.auth_create_profile_submit),
                            size = ButtonProperties.Size.Large,
                            state =
                                if (viewState.isFormValid && !viewState.isLoading) {
                                    ButtonProperties.State.Default
                                } else {
                                    ButtonProperties.State.Disabled
                                },
                        ),
                    onClick = {
                        onIntent(
                            CreateProfileIntent.Submit(
                                name = viewState.name,
                                dateOfBirth = viewState.dateOfBirth,
                                isMale = viewState.isMale,
                            ),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))

                Text(
                    text = stringResource(Res.string.auth_create_profile_skip),
                    modifier =
                        Modifier
                            .clickable(enabled = !viewState.isLoading) {
                                onIntent(CreateProfileIntent.Skip)
                            }
                            .padding(vertical = TokenProvider.spacings.sm),
                    style = TokenProvider.textStyles.bodyStrong,
                    color = TokenProvider.colors.textMuted,
                )
            }
        }

        if (viewState.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = TokenProvider.colors.bgAccent)
            }
        }
    }
}

@Composable
private fun AvatarGrid(
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(PROFILE_AVATARS) { index, emoji ->
            val isSelected = index == selected
            val bg = AVATAR_BACKGROUNDS.getOrElse(index) { Color(0xFFFFE796) }
            Box(
                modifier =
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(bg)
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    width = 3.dp,
                                    color = TokenProvider.colors.borderAccent,
                                    shape = CircleShape,
                                )
                            } else {
                                Modifier
                            },
                        )
                        .clickable { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = emoji,
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                )
            }
        }
    }
}

@Composable
private fun GenderSelector(
    isMale: Boolean,
    enabled: Boolean,
    onSelect: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
    ) {
        GenderOption(
            label = stringResource(Res.string.auth_create_profile_gender_boy),
            emoji = "👦",
            selected = isMale,
            enabled = enabled,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(true) },
        )
        GenderOption(
            label = stringResource(Res.string.auth_create_profile_gender_girl),
            emoji = "👧",
            selected = !isMale,
            enabled = enabled,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(false) },
        )
    }
}

@Composable
private fun GenderOption(
    label: String,
    emoji: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(
                    if (selected) TokenProvider.colors.bgAccentSoft else TokenProvider.colors.bgElevated,
                )
                .then(
                    if (selected) {
                        Modifier.border(
                            width = 2.dp,
                            color = TokenProvider.colors.borderAccent,
                            shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                        )
                    } else {
                        Modifier
                    },
                )
                .clickable(enabled = enabled, onClick = onClick)
                .padding(vertical = TokenProvider.spacings.lg),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
        ) {
            Text(text = emoji, fontSize = 32.sp, lineHeight = 36.sp)
            Text(
                text = label,
                style =
                    TokenProvider.textStyles.bodyStrong.copy(
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                    ),
                color = if (selected) TokenProvider.colors.textAccent else TokenProvider.colors.textMuted,
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        style = TokenProvider.textStyles.bodyStrong,
        color = TokenProvider.colors.text,
    )
}
