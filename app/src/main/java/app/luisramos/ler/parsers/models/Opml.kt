package app.luisramos.ler.parsers.models

data class Opml(
    val title: String,
    val items: List<Outline>
) {
    data class Outline(
        val text: String,
        val type: String,
        val xmlUrl: String,
        val htmlUrl: String
    )
}