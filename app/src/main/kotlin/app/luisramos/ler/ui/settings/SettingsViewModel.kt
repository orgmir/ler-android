package app.luisramos.ler.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import app.luisramos.ler.R
import app.luisramos.ler.domain.Preferences
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.navigation.Navigation

class SettingsViewModel(
    parentViewModel: ScaffoldViewModel,
    private val navigation: Navigation,
    private val preferences: Preferences
) : ViewModel() {

    private val timeLiveData = MutableLiveData<TimeUiModel>()
    private val formattedTime = timeLiveData.map {
        val (hour, minute) = it
        "%02d:%02d".format(hour, minute)
    }

    val items = formattedTime.map { time ->
        listOf(
            SettingsUiModel.Switch(
                title = R.string.settings_show_read_feed_items,
                isChecked = preferences.showReadFeedItems
            ),
            SettingsUiModel.TimePicker(
                R.string.settings_new_post_notif_time,
                R.string.settings_new_post_notif_desc,
                time = time
            )
        )
    }

    private val timeObserver: Observer<TimeUiModel> = Observer {
        preferences.newPostNotificationTime = it.hour * 100 + it.minute
    }

    init {
        parentViewModel.title.value = "Settings"

        val time = preferences.newPostNotificationTime
        val hour = time / 100
        val minute = time - hour * 100
        timeLiveData.value = TimeUiModel(hour, minute)
        // observe so we can save the value in preferences if the user
        // changes it
        timeLiveData.observeForever(timeObserver)
    }

    override fun onCleared() {
        timeLiveData.removeObserver(timeObserver)
        super.onCleared()
    }

    fun onItemClicked(position: Int) {
        when (position) {
            0 -> preferences.showReadFeedItems = !preferences.showReadFeedItems
            1 -> navigation.openTimePicker(timeLiveData)
        }
    }
}