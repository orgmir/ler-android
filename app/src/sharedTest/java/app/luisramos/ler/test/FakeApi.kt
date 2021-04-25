package app.luisramos.ler.test

import app.luisramos.ler.data.Api
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.URL

class FakeApi : Api {
    private var mocks = mutableMapOf<String, Response.Builder>()

    fun mockResponse(url: String, responseBuilder: Response.Builder) {
        mocks[url] = responseBuilder
    }

    override suspend fun download(url: URL): Response = download(url.toString())

    override suspend fun download(url: String): Response {
        val request = Request.Builder().url(url).build()
        val responseBuilder = mocks[url] ?: Response.Builder()
            .protocol(Protocol.HTTP_2)
            .message("")
            .code(200)
            .body("".toResponseBody())
        return responseBuilder.request(request).build()
    }
}