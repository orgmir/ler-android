package app.luisramos.thecollector.data

import timber.log.Timber
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun URL.download(): InputStream? {
    return (openConnection() as? HttpURLConnection)?.run {
        try {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}