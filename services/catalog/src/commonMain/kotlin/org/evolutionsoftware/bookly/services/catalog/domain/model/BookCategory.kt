package org.evolutionsoftware.bookly.services.catalog.domain.model

enum class BookCategory(
    val label: String,
) {
    All("All Nature"),
    Animals("Animals"),
    Plants("Plants"),
    Weather("Weather"),
    Colors("Colors"),
    Shapes("Shapes"),
    Food("Food"),
    Numbers("Numbers"),
    Birds("Birds"),
    ;

    companion object {
        fun fromName(name: String?): BookCategory {
            val normalized = name?.trim().orEmpty()
            return entries.firstOrNull {
                it != All &&
                    (it.name.equals(normalized, ignoreCase = true) ||
                        it.label.equals(normalized, ignoreCase = true))
            } ?: All
        }
    }
}
