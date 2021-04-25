package app.luisramos.ler.ui.settings

sealed class SettingsUiModel {
    data class Switch(val title: Int, val desc: Int? = null, val isChecked: Boolean) :
        SettingsUiModel()

    data class TimePicker(val title: Int, val desc: Int, val time: String) : SettingsUiModel()
}