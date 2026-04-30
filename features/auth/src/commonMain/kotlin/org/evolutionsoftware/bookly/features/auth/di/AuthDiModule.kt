package org.evolutionsoftware.bookly.features.auth.di

import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordEffectProducer
import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordIntentProcessor
import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordStateMapper
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordEffectProducer
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordIntentProcessor
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordStateMapper
import org.evolutionsoftware.bookly.features.auth.signin.SignInEffectProducer
import org.evolutionsoftware.bookly.features.auth.signin.SignInIntentProcessor
import org.evolutionsoftware.bookly.features.auth.signin.SignInStateMapper
import org.evolutionsoftware.bookly.features.auth.signup.SignUpEffectProducer
import org.evolutionsoftware.bookly.features.auth.signup.SignUpIntentProcessor
import org.evolutionsoftware.bookly.features.auth.signup.SignUpStateMapper
import org.koin.dsl.module

object AuthDiModule {
    val module =
        module {
            factory { SignInIntentProcessor(get()) }
            factory { SignInStateMapper() }
            factory { SignInEffectProducer() }

            factory { SignUpIntentProcessor(get()) }
            factory { SignUpStateMapper() }
            factory { SignUpEffectProducer() }

            factory { ChangePasswordIntentProcessor() }
            factory { ChangePasswordStateMapper() }
            factory { ChangePasswordEffectProducer() }

            factory { ResetPasswordIntentProcessor() }
            factory { ResetPasswordStateMapper() }
            factory { ResetPasswordEffectProducer() }
        }
}
