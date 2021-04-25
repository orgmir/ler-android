package app.luisramos.ler.ui.settings

import androidx.lifecycle.*
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

    private val notifTimeLiveData = MutableLiveData<TimeUiModel>()
    private val formattedTime = notifTimeLiveData.map {
        val (hour, minute) = it
        "%02d:%02d".format(hour, minute)
    }

    private val refreshIntervalLiveData = MutableLiveData<Int>()
    private val formattedRefreshInterval = refreshIntervalLiveData.map { hours ->
        "%02dh".format(hours)
    }

    val items = MediatorLiveData<Pair<String, String>>().apply {
        addSource(formattedTime) { time ->
            value = time to value?.second.orEmpty()
        }
        addSource(formattedRefreshInterval) { interval ->
            value = value?.first.orEmpty() to interval
        }
    }.map {
        val (time, interval) = it
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
            ),
            SettingsUiModel.Switch(
                title = R.string.settings_refresh_feeds_switch,
                desc = R.string.settings_refresh_feed_desc,
                isChecked = preferences.isFeedRefreshEnabled
            )
            // LR: Don't allow configuration for now, feed refreshes every 12 hours
//            SettingsUiModel.TimePicker(
//                title = R.string.settings_refresh_feed_time,
//                desc = R.string.settings_refresh_feed_desc,
//                time = interval
//            )
        )
    }

    init {
        parentViewModel.title.value = "Settings"

        val (hour, minute) = newPostsNotificationPreferencesUseCase.notifyHourMinute
        notifTimeLiveData.value = TimeUiModel(hour, minute)
        // observe so we can save the value in preferences if the user
        // changes it
        notifTimeLiveData.observeForever(::saveTime)
    }

    override fun onCleared() {
        notifTimeLiveData.removeObserver(::saveTime)
        super.onCleared()
    }

    fun onItemClicked(position: Int) {
        when (position) {
            0 -> preferences.hideReadFeedItems = !preferences.hideReadFeedItems
            1 -> preferences.isNewPostNotificationEnabled =
                !preferences.isNewPostNotificationEnabled
            2 -> navigation.openTimePicker(notifTimeLiveData)
            4 -> preferences.isFeedRefreshEnabled = !preferences.isFeedRefreshEnabled
        }
    }

    private fun saveTime(timeUiModel: TimeUiModel) {
        viewModelScope.launch {
            val (hour, minute) = timeUiModel
            newPostsNotificationPreferencesUseCase.savePref(hour, minute)
        }
    }
}