package app.luisramos.ler.domain

interface ShowNewPostsLocalNotifUseCase {
    suspend fun showLocalNotif()
}

typealias ShowLocalNotificationAction = (List<String>) -> Unit

class DefaultShowNewPostsLocalNotifUseCase(
    val useCase: FetchFeedTitlesToNotifyUserUseCase,
    val showNotificationAction: ShowLocalNotificationAction
) : ShowNewPostsLocalNotifUseCase {
    override suspend fun showLocalNotif() {
        val titles = useCase.fetchFeedTitlesWithNewPosts()
        if (titles.isNotEmpty()) {
            showNotificationAction(titles)
        }
    }
}