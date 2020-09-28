package app.luisramos.ler.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat

fun Boolean.toggleGone(): Int = if (this) View.VISIBLE else View.GONE
fun Boolean.toggleInvisible(): Int = if (this) View.VISIBLE else View.INVISIBLE

fun View.getDimen(@AttrRes attrRes: Int): Float = getTypedValue(attrRes)?.let {
    TypedValue.complexToDimension(it.data, resources.displayMetrics)
} ?: 0f

fun View.getDrawable(@AttrRes attrRes: Int): Drawable? = getTypedValue(attrRes)?.let {
    ContextCompat.getDrawable(context, it.resourceId)
}

private fun View.getTypedValue(@AttrRes attrRes: Int): TypedValue? {
    val typedValue = TypedValue()
    return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
        typedValue
    } else {
        null
    }
}

fun View.hideKeyboard() {
    context.getSystemService(InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}