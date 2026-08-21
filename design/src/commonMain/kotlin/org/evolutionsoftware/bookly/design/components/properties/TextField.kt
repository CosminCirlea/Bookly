package org.evolutionsoftware.bookly.design.components.properties

data class TextFieldProperties(
    val label: String? = null,
    val placeholder: String? = null,
    val state: State = State.Default,
) {
    enum class State {
        Default,
        Error,
        Disabled,
    }
}
