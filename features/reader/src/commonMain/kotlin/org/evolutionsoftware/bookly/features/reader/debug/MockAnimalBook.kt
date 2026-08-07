package org.evolutionsoftware.bookly.features.reader.debug

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails

/**
 * A fixed twenty-page animal story used by the debug menu's reader entry.
 *
 * Held entirely in memory so the reader can be exercised without the catalog API
 * or a network connection. Each page carries only an animal and its name — the
 * reader renders the illustration and [BookCard.title], and ignores descriptions.
 */
internal object MockAnimalBook {
    private val animals =
        listOf(
            "Fox" to "🦊",
            "Bear" to "🐻",
            "Rabbit" to "🐰",
            "Cat" to "🐱",
            "Dog" to "🐶",
            "Cow" to "🐮",
            "Pig" to "🐷",
            "Horse" to "🐴",
            "Sheep" to "🐑",
            "Monkey" to "🐵",
            "Lion" to "🦁",
            "Tiger" to "🐯",
            "Elephant" to "🐘",
            "Panda" to "🐼",
            "Koala" to "🐨",
            "Penguin" to "🐧",
            "Owl" to "🦉",
            "Frog" to "🐸",
            "Turtle" to "🐢",
            "Whale" to "🐳",
        )

    val book: BookDetails =
        BookDetails(
            id = "debug-animals",
            title = "Animal Friends",
            category = BookCategory.Animals,
            cards =
                animals.mapIndexed { index, (name, emoji) ->
                    BookCard(
                        id = "debug-animal-${index + 1}",
                        title = name,
                        description = "",
                        emoji = emoji,
                        imageUrl = null,
                    )
                },
        )
}
