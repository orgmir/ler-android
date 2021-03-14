package app.luisramos.ler.test

import app.luisramos.ler.data.Api
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.URL

class FakeApi : Api {
    var cannedResponseBuilder = Response.Builder()
        .protocol(Protocol.HTTP_2)
        .message("")
        .code(200)
        .body("".toResponseBody())

    override suspend fun download(url: URL): Response {
        val request = Request.Builder().url(url).build()
        return cannedResponseBuilder.request(request).build()
    }

    override suspend fun download(url: String): Response {
        val request = Request.Builder().url(url).build()
        return cannedResponseBuilder.request(request).build()
    }
}