package app.luisramos.ler.ui.navigation

import androidx.lifecycle.MutableLiveData
import app.luisramos.ler.ui.settings.TimeUiModel

interface Navigation {
    fun goBack()
    fun goToAddSubscriptionScreen()
    fun goToSettingsScreen()
    fun openTimePicker(time: MutableLiveData<TimeUiModel>)
    fun goToExternalBrowser(link: String)
    fun showToast(textRes: Int)


}

