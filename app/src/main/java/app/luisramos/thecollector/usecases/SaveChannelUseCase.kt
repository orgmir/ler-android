package app.luisramos.thecollector.usecases

import app.luisramos.thecollector.data.Db
import app.luisramos.thecollector.data.model.FeedUpdateMode
import com.prof.rssparser.Channel
import java.io.IOException

class SaveChannelUseCase(
    val db: Db
) {
    suspend fun saveChannel(channel: Channel): Result<Boolean> {
        val (title, link, description) = channel

        if (title == null || link == null) {
            return Result.failure(IOException("No title or link for channel"))
        }

        // TODO save description
        db.insertFeed(title, link,
            FeedUpdateMode.NONE
        )

        //TODO insert articles

        return Result.success(true)
    }
}