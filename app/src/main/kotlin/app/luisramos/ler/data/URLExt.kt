package app.luisramos.ler.data

import timber.log.Timber
import java.io.InputStream
import java.net.URL

fun <T> URL.download(block: (InputStream) -> T?) = try {
    openConnection().apply {
        readTimeout = 10000
        connectTimeout = 15000
        if (useCaches) {
            useCaches = false
        }
    }.getInputStream()
        .use(block)
} catch (e: Exception) {
    Timber.e(e)
    null
}