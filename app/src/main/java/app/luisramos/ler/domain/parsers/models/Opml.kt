package app.luisramos.ler.domain.parsers.models

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