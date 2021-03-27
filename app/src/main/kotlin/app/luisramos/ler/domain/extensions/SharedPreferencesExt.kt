package app.luisramos.ler.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> makeDelegate(
    getter: () -> T,
    setter: (T) -> Unit
) = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = getter()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = setter(value)
}

fun SharedPreferences.string(key: String, defaultValue: String = "") =
    makeDelegate(
        getter = { getString(key, defaultValue) },
        setter = { edit { putString(key, it) } }
    )

fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false) =
    makeDelegate(
        getter = { getBoolean(key, defaultValue) },
        setter = { edit { putBoolean(key, it) } }
    )

fun SharedPreferences.int(key: String, defaultValue: Int = -1) =
    makeDelegate(
        getter = { getInt(key, defaultValue) },
        setter = { edit { putInt(key, it) } }
    )

fun SharedPreferences.long(key: String, defaultValue: Long = -1L) =
    makeDelegate(
        getter = { getLong(key, defaultValue) },
        setter = { edit { putLong(key, it) } }
    )