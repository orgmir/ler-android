package app.luisramos.ler.ui.views

import android.widget.TextView
import androidx.annotation.StringRes

sealed class TextResource {
    data class Text(val text: String) : TextResource()
    data class Res(@StringRes val res: Int) : TextResource()
}

fun textResource(res: Int): TextResource = TextResource.Res(res)
fun textResource(text: String): TextResource = TextResource.Text(text)

fun TextView.setText(textResource: TextResource) {
    when (textResource) {
        is TextResource.Res -> setText(textResource.res)
        is TextResource.Text -> text = textResource.text
    }
}