package org.evolutionsoftware.bookly.features.home

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBooksUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.AddFavoriteUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.FavoritesError
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.GetFavoritesUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.RemoveFavoriteUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeIntentProcessorTest {
    @Test
    fun `a warm cache emits books without ever entering the loading state`() =
        runTest {
            val cached = listOf(book("1", BookCategory.Animals))
            val processor = processor(FakeCatalog(cached = cached, remote = cached))

            val actions = processor(HomeIntent.Load).toList()

            assertTrue(
                actions.none { it is HomeAction.LoadingStarted },
                "cached books must render immediately, with no loading state",
            )
            assertTrue(actions.first() is HomeAction.BooksLoaded)
        }

    @Test
    fun `an empty cache shows the loading state before books arrive`() =
        runTest {
            val remote = listOf(book("1", BookCategory.Animals))
            val processor = processor(FakeCatalog(cached = emptyList(), remote = remote))

            val actions = processor(HomeIntent.Load).toList()

            assertTrue(actions.first() is HomeAction.LoadingStarted)
            assertEquals(remote, actions.filterIsInstance<HomeAction.BooksLoaded>().last().books)
        }

    @Test
    fun `an unchanged refresh does not re-emit the same list`() =
        runTest {
            val cached = listOf(book("1", BookCategory.Animals))
            val processor = processor(FakeCatalog(cached = cached, remote = cached))

            val actions = processor(HomeIntent.Load).toList()

            assertEquals(
                1,
                actions.filterIsInstance<HomeAction.BooksLoaded>().size,
                "an identical refresh should not make the grid recompose",
            )
        }

    @Test
    fun `a refresh that changes the catalogue emits again`() =
        runTest {
            val cached = listOf(book("1", BookCategory.Animals))
            val remote = cached + book("2", BookCategory.Food)
            val processor = processor(FakeCatalog(cached = cached, remote = remote))

            val loaded = processor(HomeIntent.Load).toList().filterIsInstance<HomeAction.BooksLoaded>()

            assertEquals(2, loaded.size)
            assertEquals(cached, loaded.first().books)
            assertEquals(remote, loaded.last().books)
        }

    @Test
    fun `a failed refresh keeps the cached books and reports no error`() =
        runTest {
            val cached = listOf(book("1", BookCategory.Animals))
            val processor = processor(FakeCatalog(cached = cached, failRefresh = true))

            val actions = processor(HomeIntent.Load).toList()

            assertTrue(
                actions.none { it is HomeAction.LoadingFailed },
                "a failed refresh must not disturb books already on screen",
            )
            assertEquals(cached, actions.filterIsInstance<HomeAction.BooksLoaded>().single().books)
        }

    @Test
    fun `a failed refresh with nothing cached surfaces the failure`() =
        runTest {
            val processor = processor(FakeCatalog(cached = emptyList(), failRefresh = true))

            val actions = processor(HomeIntent.Load).toList()

            assertTrue(actions.any { it is HomeAction.LoadingFailed })
        }

    // === Filter options ===================================================

    @Test
    fun `filter options come from the cached books`() =
        runTest {
            val cached =
                listOf(
                    book("1", BookCategory.Animals),
                    book("2", BookCategory.Food),
                    book("3", BookCategory.Animals),
                )
            val mapper = HomeStateMapper()
            val state = mapper(HomeAction.BooksLoaded(cached), HomeViewState())

            assertEquals(
                listOf(BookCategory.All, BookCategory.Animals, BookCategory.Food),
                state.categories,
                "options should be All plus each category present, without duplicates",
            )
        }

    @Test
    fun `a selected category that disappears falls back to All`() =
        runTest {
            val mapper = HomeStateMapper()
            val withFood =
                mapper(
                    HomeAction.BooksLoaded(listOf(book("1", BookCategory.Food))),
                    HomeViewState(selectedCategory = BookCategory.Food),
                )
            assertEquals(BookCategory.Food, withFood.selectedCategory)

            // The refresh drops every Food book.
            val afterRefresh =
                mapper(HomeAction.BooksLoaded(listOf(book("2", BookCategory.Animals))), withFood)

            assertEquals(BookCategory.All, afterRefresh.selectedCategory)
            assertEquals(1, afterRefresh.visibleBooks.size, "books must not be filtered away")
        }
}

// === Fixtures =============================================================

private fun book(
    id: String,
    category: BookCategory,
) = BookSummary(
    id = id,
    title = "Book $id",
    description = "",
    category = category,
    emoji = "",
    imageUrl = null,
)

private fun processor(catalog: CatalogRepository) =
    HomeIntentProcessor(
        getBooksUseCase = GetBooksUseCase(catalog),
        getCurrentProfileUseCase = GetCurrentProfileUseCase(NoProfile),
        getFavoritesUseCase = NoFavorites,
        addFavoriteUseCase = NoAddFavorite,
        removeFavoriteUseCase = NoRemoveFavorite,
    )

private class FakeCatalog(
    private val cached: List<BookSummary>,
    private val remote: List<BookSummary> = emptyList(),
    private val failRefresh: Boolean = false,
) : CatalogRepository {
    override suspend fun getBooks(refresh: CatalogRefresh): List<BookSummary> =
        when (refresh) {
            CatalogRefresh.CacheOnly -> cached
            else -> {
                // Mirrors the repository contract: a failed refresh falls back to the
                // cache, and only throws when the cache is empty.
                if (failRefresh) {
                    if (cached.isEmpty()) error("network down") else cached
                } else {
                    remote
                }
            }
        }

    override suspend fun getBookDetails(
        bookId: String,
        refresh: CatalogRefresh,
    ): BookDetails? = null
}

private object NoProfile : ProfileRepository {
    override suspend fun getCurrentProfile(): ParentProfile? = null

    override suspend fun login(displayName: String): ParentProfile = error("unused")

    override suspend fun register(displayName: String): ParentProfile = error("unused")

    override suspend fun logout() = Unit

    override suspend fun createProfile(
        name: String,
        dateOfBirth: String,
        gender: Boolean,
    ): ParentProfile = error("unused")
}

private object NoFavorites : GetFavoritesUseCase {
    override suspend fun invoke(
        profileId: String,
        languageId: Int,
    ): Result<List<Favorite>, FavoritesError> = Result.Success(emptyList())
}

private object NoAddFavorite : AddFavoriteUseCase {
    override suspend fun invoke(
        bookId: String,
        profileId: String,
    ): Result<Unit, FavoritesError> = Result.Success(Unit)
}

private object NoRemoveFavorite : RemoveFavoriteUseCase {
    override suspend fun invoke(
        profileId: String,
        bookId: String,
    ): Result<Unit, FavoritesError> = Result.Success(Unit)
}
