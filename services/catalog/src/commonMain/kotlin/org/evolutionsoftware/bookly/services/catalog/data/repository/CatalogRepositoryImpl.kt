package org.evolutionsoftware.bookly.services.catalog.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookCardDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDto
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toDetails
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class CatalogRepositoryImpl : CatalogRepository {
    private val mutex = Mutex()
    private var cachedBooks: List<BookDto>? = null

    override suspend fun getBooks(forceRefresh: Boolean): List<BookSummary> =
        loadCatalog(forceRefresh).map(BookDto::toSummary)

    override suspend fun getBookDetails(
        bookId: String,
        forceRefresh: Boolean,
    ): BookDetails? = loadCatalog(forceRefresh).firstOrNull { it.id == bookId }?.toDetails()

    private suspend fun loadCatalog(forceRefresh: Boolean): List<BookDto> =
        mutex.withLock {
            // Bookly uses cache-first reads so the first successful load becomes the offline source.
            if (!forceRefresh) {
                cachedBooks?.let { return it }
            }
            val fresh = CatalogRemoteDataSource.fetchCatalog()
            cachedBooks = fresh
            fresh
        }
}

private object CatalogRemoteDataSource {
    fun fetchCatalog(): List<BookDto> =
        listOf(
            BookDto(
                id = "forest-animals",
                title = "Forest Animals",
                description = "Explore a cozy woodland with five friendly forest animals.",
                category = "Animals",
                emoji = "🦊",
                cards =
                    listOf(
                        BookCardDto("fox", "Fox", "A fox is a quick animal with a fluffy tail.", "🦊"),
                        BookCardDto("owl", "Owl", "Owls are clever birds that love quiet forests.", "🦉"),
                        BookCardDto("bear", "Bear", "Bears are strong animals with thick warm fur.", "🐻"),
                        BookCardDto("deer", "Deer", "A deer moves softly through the forest.", "🦌"),
                        BookCardDto("squirrel", "Squirrel", "Squirrels hide nuts high in the trees.", "🐿"),
                    ),
            ),
            BookDto(
                id = "birds",
                title = "Birds",
                description = "Meet bright, chirpy birds from treetops and meadows.",
                category = "Animals",
                emoji = "🐦",
                cards =
                    listOf(
                        BookCardDto("sparrow", "Sparrow", "Sparrows are tiny birds with busy little wings.", "🐦"),
                        BookCardDto("owl-night", "Night Owl", "Owls can see well when the moon comes out.", "🦉"),
                        BookCardDto("duck", "Duck", "Ducks glide across ponds with gentle splashes.", "🦆"),
                        BookCardDto("parrot", "Parrot", "Parrots love bright feathers and loud chatter.", "🦜"),
                    ),
            ),
            BookDto(
                id = "garden-veggies",
                title = "Garden Veggies",
                description = "Fresh vegetables growing in soft soil and sunshine.",
                category = "Plants",
                emoji = "🥕",
                cards =
                    listOf(
                        BookCardDto("carrot", "Carrot", "Carrots are orange vegetables that grow underground.", "🥕"),
                        BookCardDto("lettuce", "Lettuce", "Lettuce leaves are soft and crunchy.", "🥬"),
                        BookCardDto("tomato", "Tomato", "Tomatoes can be juicy and bright red.", "🍅"),
                        BookCardDto("pumpkin", "Pumpkin", "Pumpkins grow wide and round in the garden.", "🎃"),
                    ),
            ),
            BookDto(
                id = "bugs-insects",
                title = "Bugs & Insects",
                description = "Little crawlers and fluttering wings from the garden floor.",
                category = "Animals",
                emoji = "🐞",
                cards =
                    listOf(
                        BookCardDto("ladybug", "Ladybug", "Ladybugs rest on leaves with tiny spotted shells.", "🐞"),
                        BookCardDto("butterfly", "Butterfly", "Butterflies float softly over flowers.", "🦋"),
                        BookCardDto("bee", "Bee", "Bees help flowers grow by carrying pollen.", "🐝"),
                        BookCardDto("beetle", "Beetle", "Beetles have shiny backs and strong legs.", "🪲"),
                    ),
            ),
            BookDto(
                id = "river-life",
                title = "River Life",
                description = "Glassy water, cool currents, and creatures by the riverbank.",
                category = "Weather",
                emoji = "💧",
                cards =
                    listOf(
                        BookCardDto("river", "River", "A river moves water from the hills to the sea.", "💧"),
                        BookCardDto("frog", "Frog", "Frogs love cool water and splashy reeds.", "🐸"),
                        BookCardDto("fish", "Fish", "Fish flick their tails beneath the water.", "🐟"),
                        BookCardDto("bridge", "Bridge", "A bridge stretches over the calm river.", "🌉"),
                        BookCardDto("stone", "Stone", "Smooth stones rest quietly by the shore.", "🪨"),
                    ),
            ),
            BookDto(
                id = "night-sky",
                title = "Night Sky",
                description = "Moonlight, stars, and bedtime wonder above the playroom.",
                category = "Weather",
                emoji = "🌙",
                cards =
                    listOf(
                        BookCardDto("moon", "Moon", "The moon glows softly when the sky turns dark.", "🌙"),
                        BookCardDto("star", "Star", "Stars sparkle like tiny lanterns in the sky.", "⭐"),
                        BookCardDto("cloud", "Cloud", "Some clouds drift past the moon at night.", "☁️"),
                        BookCardDto("owl-night", "Night Owl", "An owl watches quietly from a moonlit branch.", "🦉"),
                    ),
            ),
        )
}
