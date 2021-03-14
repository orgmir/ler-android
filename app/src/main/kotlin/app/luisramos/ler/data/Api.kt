package app.luisramos.ler.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URL

object Api {
    val coroutineContext = Dispatchers.IO
    private val client = OkHttpClient.Builder().build()

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun download(url: URL): Response = withContext(coroutineContext) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun download(url: String): Response = withContext(coroutineContext) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute()
    }
}