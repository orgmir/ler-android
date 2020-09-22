package app.luisramos.ler

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.luisramos.ler.di.AppContainer
import app.luisramos.ler.ui.main.MainFragment
import app.luisramos.ler.ui.subscription.AddSubscriptionFragment
import app.luisramos.ler.domain.work.FeedUpdateWorker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ADD_FRAG_TAG = "ADD_FRAG_TAG"
    }

    private val appContainer: AppContainer
        get() = (application as App).appContainer
    private val parentViewModel: ParentViewModel by viewModels {
        appContainer.activityViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), "main_frag")
                .commitNow()
        }

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            when {
                supportFragmentManager.backStackEntryCount > 0 ->
                    supportFragmentManager.popBackStack()
                else -> drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleIntentSend()
        }

        parentViewModel.selectedFeed.observe(this, {
            drawerLayout.closeDrawer(GravityCompat.START)
        })
        parentViewModel.title.observe(this, {
            toolbar.title = it
        })

        toolbarProgress.pivotX = 0f
        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(UPDATE_WORK_ID)
            .observe(this, {
                val workInfo = it.firstOrNull()
                val percent = when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> workInfo.progress.getFloat(
                        FeedUpdateWorker.Progress,
                        0.10f
                    )
                    else -> 1f
                }

                toolbarProgress.animate()
                    .scaleX(percent)
                    .setDuration(150)
                    .start()
            })
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 ->
                supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    private fun handleIntentSend() {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val oldFrag = supportFragmentManager.findFragmentByTag(ADD_FRAG_TAG)
            oldFrag?.let {
                supportFragmentManager.beginTransaction()
                    .remove(oldFrag)
                    .commitAllowingStateLoss()
            }

            val frag = AddSubscriptionFragment.newInstance(it)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, frag, ADD_FRAG_TAG)
                .addToBackStack(null)
                .commit()
        }
    }
}

