package app.luisramos.ler.ui.sidemenu

import app.luisramos.ler.data.FeedsWithCount
import app.luisramos.ler.domain.FakeFetchFeedUseCase
import app.luisramos.ler.domain.FakeFetchFeedsUseCase
import app.luisramos.ler.domain.Result
import app.luisramos.ler.test.CoroutineDispatcherRule
import app.luisramos.ler.test.InstantTaskExecutorRule
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.views.data
import com.google.common.truth.Truth.assertThat
import dev.luisramos.kroclin.snapshot.extensions.assertSnapshot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SideMenuViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val feedUseCase = FakeFetchFeedUseCase()
    private val feedsUseCase = FakeFetchFeedsUseCase()
    private lateinit var scaffoldViewModel: ScaffoldViewModel
    private lateinit var viewModel: SideMenuViewModel

    @Before
    fun prepareData() {
        feedsUseCase.mockFlow = flowOf(
            Result.success(
                listOf(
                    FeedsWithCount(
                        id = -1,
                        title = "All",
                        itemsCount = 0.0,
                        titleOrder = null
                    ),
                    FeedsWithCount(
                        id = 1,
                        title = "Feed #1",
                        itemsCount = 0.0,
                        titleOrder = null
                    ),
                    FeedsWithCount(
                        id = 2,
                        title = "Feed #2",
                        itemsCount = 32.0,
                        titleOrder = null
                    )
                )
            )
        )
    }

    private fun setUp() {
        scaffoldViewModel = ScaffoldViewModel(feedUseCase)
        viewModel = SideMenuViewModel(scaffoldViewModel, feedsUseCase)
    }

    @Test
    fun `given a success fetch result, should load feeds on init`() = runBlockingTest {
        setUp()
        viewModel.uiState.value?.data?.assertSnapshot()
    }

    @Test
    fun `when selecting feed, should update ui and parent view model`() = runBlockingTest {
        setUp()

        viewModel.onItemTapped(1)

        viewModel.uiState.value?.data?.assertSnapshot()
        assertThat(scaffoldViewModel.selectedFeed.value).isEqualTo(1)
    }
}