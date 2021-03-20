package app.luisramos.ler.ui.navigation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import app.luisramos.ler.ui.screen.NavigatingActivity
import app.luisramos.ler.ui.settings.SettingsScreen
import app.luisramos.ler.ui.settings.TimePickerSettingsScreen
import app.luisramos.ler.ui.settings.TimeUiModel
import app.luisramos.ler.ui.subscription.AddSubscriptionScreen

class Navigator(
    private val activity: NavigatingActivity
) : Navigation {

    override fun goBack() {
        activity.goBack()
    }

    override fun goToAddSubscriptionScreen() {
        activity.goTo(AddSubscriptionScreen())
    }

    override fun goToSettingsScreen() {
        activity.goTo(SettingsScreen())
    }

    override fun goToExternalBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        ContextCompat.startActivity(activity, intent, null)
    }

    override fun openTimePicker(time: MutableLiveData<TimeUiModel>) {
        activity.goTo(TimePickerSettingsScreen(time))
    }

    override fun showToast(textRes: Int) {
        val text = activity.resources.getString(textRes)
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}