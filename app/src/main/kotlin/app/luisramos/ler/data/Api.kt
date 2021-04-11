package app.luisramos.ler.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URL
import kotlin.coroutines.CoroutineContext

interface Api {
    suspend fun download(url: URL): Response
    suspend fun download(url: String): Response
}

class DefaultApi(
    private val coroutineContext: CoroutineContext
) : Api {
    private val client = OkHttpClient.Builder().build()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun download(url: URL): Response = withContext(coroutineContext) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun download(url: String): Response = withContext(coroutineContext) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute()
    }
}