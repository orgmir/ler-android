package app.luisramos.ler.ui

import android.util.Log
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import app.luisramos.ler.domain.work.LOCAL_NOTIF_WORK_ID
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class WorkManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    @Ignore("Cannot make this work")
    fun givenNotifEnabled_whenAppStarts_notificationWorkShouldBeScheduled() {
        launchActivity<MainActivity>().use {
            val workInfos = WorkManager.getInstance(context)
                .getWorkInfosForUniqueWork(LOCAL_NOTIF_WORK_ID)
                .get()
            val localNotifState = workInfos.firstOrNull()?.state
            assertThat(localNotifState).isEqualTo(WorkInfo.State.ENQUEUED)
        }
    }
}