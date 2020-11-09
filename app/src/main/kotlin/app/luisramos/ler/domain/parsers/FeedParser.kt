package app.luisramos.ler.domain.parsers

import app.luisramos.ler.data.model.FeedModel
import java.io.InputStream

interface FeedParser {
    fun parse(inputStream: InputStream): FeedModel?
}