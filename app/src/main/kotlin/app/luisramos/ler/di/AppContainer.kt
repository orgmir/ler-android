package app.luisramos.ler.di

import androidx.work.WorkerFactory
import app.luisramos.ler.data.Api
import app.luisramos.ler.domain.*
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.navigation.Navigation
import app.luisramos.ler.ui.screen.NavigatingActivity

interface AppContainer : UseCaseContainer, FactoryContainer, NavigationContainer {
    val db: Db
    val preferences: Preferences
    val api: Api
}

interface UseCaseContainer {
    val fetchChannelUseCase: FetchChannelUseCase
    val fetchFeedsUseCase: FetchFeedsUseCase
    val saveFeedUseCase: SaveFeedUseCase
    val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
    val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase
    val fetchFeedItemsUseCase: FetchFeedItemsUseCase
    val setFeedItemUnreadUseCase: SetUnreadFeedItemUseCase
    val fetchFeedUseCase: FetchFeedUseCase
    val refreshFeedsUseCase: RefreshFeedsUseCase
    val deleteFeedUseCase: DeleteFeedUseCase
    val toggleNotifyMeFeedUseCase: ToggleNotifyMeFeedUseCase
    val newPostsNotificationPreferencesUseCase: NewPostsNotificationPreferencesUseCase
    val scheduleNewPostsNotifUseCase: ScheduleNewPostsNotifUseCase
    val fetchFeedTitlesToNotifyUserUseCase: FetchFeedTitlesToNotifyUserUseCase
    val showNewPostsLocalNotifUseCase: ShowNewPostsLocalNotifUseCase
    val cancelNewPostsNotifUseCase: CancelNewPostsNotificationUseCase
}

interface FactoryContainer {
    val workerFactory: WorkerFactory
    val activityViewModelProviderFactory: ActivityViewModelProviderFactory
    fun getViewModelFactory(parentViewModel: ScaffoldViewModel): ViewModelProviderFactory
}

interface NavigationContainer {
    val navigation: Navigation
    fun bindNavigation(navigatingActivity: NavigatingActivity)
}