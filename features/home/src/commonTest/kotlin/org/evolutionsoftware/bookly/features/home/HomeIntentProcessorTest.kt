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
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.categories.domain.usecase.GetCategoriesError
import org.evolutionsoftware.bookly.services.categories.domain.usecase.GetCategoriesUseCase
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
    fun `load emits filters from the categories API use case`() =
        runTest {
            val categories = listOf(Category(id = "10", name = "Animals"))
            val processor =
                processor(
                    catalog = FakeCatalog(cached = emptyList(), remote = emptyList()),
                    categories = StaticCategories(categories),
                )

            val actions = processor(HomeIntent.Load).toList()

            assertEquals(
                categories,
                actions.filterIsInstance<HomeAction.CategoriesLoaded>().single().categories,
            )
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

    @Test
    fun `favorite toggle is reverted when there is no active profile`() =
        runTest {
            val processor = processor(FakeCatalog(cached = emptyList()))

            val actions =
                processor(
                    HomeIntent.FavoriteToggled(
                        bookId = "1",
                        makeFavorite = true,
                    ),
                ).toList()

            assertEquals(
                listOf(
                    HomeAction.FavoriteUpdateReverted(bookId = "1", isFavorite = false),
                ),
                actions,
            )
        }

    // === Filter options ===================================================

    @Test
    fun `filter options come from the categories API`() =
        runTest {
            val categories =
                listOf(
                    Category(id = "10", name = "Animals"),
                    Category(id = "20", name = "Food"),
                )
            val mapper = HomeStateMapper()
            val state = mapper(HomeAction.CategoriesLoaded(categories), HomeViewState())

            assertEquals(categories, state.categories)
        }

    @Test
    fun `a selected API category that disappears falls back to all books`() =
        runTest {
            val mapper = HomeStateMapper()
            val books =
                listOf(
                    book("1", BookCategory.Food, categoryId = "20"),
                    book("2", BookCategory.Animals, categoryId = "10"),
                )
            val withBooks =
                mapper(
                    HomeAction.BooksLoaded(books),
                    HomeViewState(selectedCategoryId = "20"),
                )
            val withFood =
                mapper(
                    HomeAction.CategoriesLoaded(listOf(Category(id = "20", name = "Food"))),
                    withBooks,
                )
            assertEquals("20", withFood.selectedCategoryId)
            assertEquals(listOf("1"), withFood.visibleBooks.map { it.id })

            val afterRefresh =
                mapper(
                    HomeAction.CategoriesLoaded(listOf(Category(id = "10", name = "Animals"))),
                    withFood,
                )

            assertEquals(null, afterRefresh.selectedCategoryId)
            assertEquals(books, afterRefresh.visibleBooks)
        }
}

// === Fixtures =============================================================

private fun book(
    id: String,
    category: BookCategory,
    categoryId: String = category.name,
) = BookSummary(
    id = id,
    title = "Book $id",
    description = "",
    category = category,
    emoji = "",
    imageUrl = null,
    categoryIds = setOf(categoryId),
)

private fun processor(
    catalog: CatalogRepository,
    categories: GetCategoriesUseCase = NoCategories,
) =
    HomeIntentProcessor(
        getBooksUseCase = GetBooksUseCase(catalog),
        getCategoriesUseCase = categories,
        getCurrentProfileUseCase = GetCurrentProfileUseCase(NoProfile),
        getFavoritesUseCase = NoFavorites,
        addFavoriteUseCase = NoAddFavorite,
        removeFavoriteUseCase = NoRemoveFavorite,
    )

private object NoCategories : GetCategoriesUseCase {
    override suspend fun invoke(languageId: Int): Result<List<Category>, GetCategoriesError> =
        Result.Success(emptyList())
}

private class StaticCategories(
    private val categories: List<Category>,
) : GetCategoriesUseCase {
    override suspend fun invoke(languageId: Int): Result<List<Category>, GetCategoriesError> =
        Result.Success(categories)
}

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
