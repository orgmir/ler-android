package app.luisramos.ler.ui

import androidx.lifecycle.MutableLiveData
import app.luisramos.ler.ui.navigation.Navigation
import app.luisramos.ler.ui.settings.TimeUiModel

class FakeNavigation : Navigation {
    override fun goBack() {

    }

    override fun goToAddSubscriptionScreen() {

    }

    override fun goToSettingsScreen() {

    }

    override fun openTimePicker(time: MutableLiveData<TimeUiModel>) {

    }

    override fun goToExternalBrowser(link: String) {

    }

    override fun showToast(textRes: Int) {

    }
}