package app.luisramos.ler.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.R
import app.luisramos.ler.domain.Preferences
import app.luisramos.ler.domain.NewPostsNotificationPreferencesUseCase
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.navigation.Navigation
import kotlinx.coroutines.launch

class SettingsViewModel(
    parentViewModel: ScaffoldViewModel,
    private val newPostsNotificationPreferencesUseCase: NewPostsNotificationPreferencesUseCase,
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
                title = R.string.settings_hide_read_feed_items,
                isChecked = preferences.hideReadFeedItems
            ),
            SettingsUiModel.Switch(
                title = R.string.settings_new_post_notif_switch,
                isChecked = preferences.isNewPostNotificationEnabled
            ),
            SettingsUiModel.TimePicker(
                title = R.string.settings_new_post_notif_time,
                desc = R.string.settings_new_post_notif_desc,
                time = time
            )
        )
    }

    init {
        parentViewModel.title.value = "Settings"

        val (hour, minute) = newPostsNotificationPreferencesUseCase.notifyHourMinute
        timeLiveData.value = TimeUiModel(hour, minute)
        // observe so we can save the value in preferences if the user
        // changes it
        timeLiveData.observeForever(::saveTime)
    }

    override fun onCleared() {
        timeLiveData.removeObserver(::saveTime)
        super.onCleared()
    }

    fun onItemClicked(position: Int) {
        when (position) {
            0 -> preferences.hideReadFeedItems = !preferences.hideReadFeedItems
            1 -> preferences.isNewPostNotificationEnabled =
                !preferences.isNewPostNotificationEnabled
            // timeLiveData will be udpated with the new time
            2 -> navigation.openTimePicker(timeLiveData)
        }
    }

    private fun saveTime(timeUiModel: TimeUiModel) {
        viewModelScope.launch {
            val (hour, minute) = timeUiModel
            newPostsNotificationPreferencesUseCase.savePref(hour, minute)
        }
    }
}