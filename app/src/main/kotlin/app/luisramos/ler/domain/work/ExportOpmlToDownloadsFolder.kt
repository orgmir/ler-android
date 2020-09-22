package app.luisramos.ler.domain.work

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import app.luisramos.ler.domain.parsers.OpmlWriter
import app.luisramos.ler.domain.parsers.models.Opml
import okhttp3.internal.closeQuietly
import java.io.File
import java.io.IOException
import java.io.OutputStream

class ExportOpmlToDownloadsFolder : (Context, Opml) -> Result<Boolean> {
    val filename = "ler-export.xml"

    override fun invoke(context: Context, opml: Opml): Result<Boolean> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveToDownloadsFolderApiQ(context, opml)
        } else {
            saveToDowloadsFolderOld(context, opml)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToDownloadsFolderApiQ(context: Context, opml: Opml): Result<Boolean> {
        val relativePath = Environment.DIRECTORY_DOWNLOADS + filename
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/xml")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        val contentResolver = context.contentResolver
        val uri =
            contentResolver.insert(MediaStore.Downloads.INTERNAL_CONTENT_URI, contentValues)
                ?: return Result.failure(IOException("Failed to create new MediaStore record"))

        val outputStream = contentResolver.openOutputStream(uri)
            ?: return Result.failure(IOException("Failed to get output stream"))

        return writeOpmlToOutputStream(outputStream, opml)
    }

    @Suppress("DEPRECATION")
    private fun saveToDowloadsFolderOld(context: Context, opml: Opml): Result<Boolean> {
        val dir = File("//sdcard//Download//")
        val file = File(dir, filename)

        val writeResult = writeOpmlToOutputStream(file.outputStream(), opml)
        if (writeResult.isFailure) {
            return writeResult
        }

        context.getSystemService<DownloadManager>()?.addCompletedDownload(
            file.name,
            file.name,
            true,
            "text/xml",
            file.absolutePath,
            file.length(),
            true
        )

        return Result.success(true)
    }

    private fun writeOpmlToOutputStream(outputStream: OutputStream, opml: Opml): Result<Boolean> =
        try {
            OpmlWriter().write(outputStream, opml)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            outputStream.closeQuietly()
        }
}