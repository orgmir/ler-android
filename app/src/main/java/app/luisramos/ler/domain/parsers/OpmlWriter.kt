package app.luisramos.ler.domain.parsers

import app.luisramos.ler.domain.parsers.models.Opml
import java.io.OutputStream
import java.io.Writer

private const val OPML_TAG = "opml"
private const val HEAD_TAG = "head"
private const val TITLE_TAG = "title"
private const val BODY_TAG = "body"
private const val OUTLINE_TAG = "outline"

class OpmlWriter {

    fun write(outputStream: OutputStream, opml: Opml) {
        outputStream.writer().use { writer ->
            writer.write("<$OPML_TAG version=\"2.0\">")
            writeHeader(writer, opml)
            writer.write("<$BODY_TAG>")
            opml.items.forEach { writeOutline(writer, it) }
            writer.write("</$BODY_TAG>")
            writer.write("</$OPML_TAG>")
        }
    }

    private fun writeHeader(writer: Writer, opml: Opml) {
        writer.write("<$HEAD_TAG>")
        writer.write("<$TITLE_TAG>")
        writer.write(opml.title)
        writer.write("</$TITLE_TAG>")
        writer.write("</$HEAD_TAG>")
    }

    private fun writeOutline(writer: Writer, outline: Opml.Outline) {
        writer.write(
            "<$OUTLINE_TAG " +
                    "text=\"${outline.text}\" " +
                    "type=\"${outline.type}\" " +
                    "htmlUrl=\"${outline.htmlUrl}\" " +
                    "xmlUrl=\"${outline.xmlUrl}\"/>"
        )
    }
}