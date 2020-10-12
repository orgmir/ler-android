package app.luisramos.ler.domain

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class DefaultFetchAndSaveChannelUseCaseTest {

    private val fetchUseCase = FakeFetchChannelUseCase()
    private val saveFeedUseCase = FakeSaveFeedUseCase()
    private val useCase = DefaultFetchAndSaveChannelUseCase(fetchUseCase, saveFeedUseCase)

    @Test
    fun `should fetch and save feed correctly`() = runBlockingTest {
        val result = useCase.fetchAndSaveChannel("http://example.com")

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `given an error fetching feeds, should not save`() = runBlockingTest {
        fetchUseCase.mockResult = Result.failure(Throwable("error"))

        val result = useCase.fetchAndSaveChannel("http://example.com")

        assertThat(result.isFailure).isTrue()
    }
}