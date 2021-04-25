package app.luisramos.ler.ui.navigation

import androidx.lifecycle.MutableLiveData
import app.luisramos.ler.ui.settings.TimeUiModel

interface Navigation {
    fun goBack()
    fun goToAddSubscriptionScreen()
    fun goToSettingsScreen()

    /**
     * Opens the time picker and uses the passed in live data
     * to pre-fill the UI and update when the user is done
     */
    fun openTimePicker(time: MutableLiveData<TimeUiModel>)
    fun goToExternalBrowser(link: String)
    fun showToast(textRes: Int)


}

