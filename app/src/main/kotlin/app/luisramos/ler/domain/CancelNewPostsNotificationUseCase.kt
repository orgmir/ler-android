package app.luisramos.ler.domain

interface CancelNewPostsNotificationUseCase {
    fun cancelScheduledNotification()
}

typealias CancelLocalNotifAction = () -> Unit

class DefaultCancelNewPostsNotificationUseCase(
    private val cancelLocalNotifAction: CancelLocalNotifAction
) : CancelNewPostsNotificationUseCase {
    override fun cancelScheduledNotification() {
        cancelLocalNotifAction()
    }
}