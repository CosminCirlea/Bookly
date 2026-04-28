package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
internal fun AuthScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
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
                        title = title,
                        variant = HeaderProperties.Variant.Compact,
                    ),
                onBackClick = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = TokenProvider.spacings.lg)
                    .padding(
                        top = TokenProvider.spacings.xl,
                        bottom = TokenProvider.spacings.xl + TokenProvider.spacings.md,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            content = content,
        )
    }
}
