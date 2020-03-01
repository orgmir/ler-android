package app.luisramos.thecollector

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import app.luisramos.thecollector.di.AppContainer
import app.luisramos.thecollector.ui.main.MainFragment
import app.luisramos.thecollector.ui.subscription.AddSubscriptionFragment
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : AppCompatActivity() {

    val appContainer: AppContainer
        get() = (application as App).appContainer
    val parentViewModel: ParentViewModel by viewModels {
        appContainer.activityViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), "main_frag")
                .commitNow()
        }

        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            when {
                supportFragmentManager.backStackEntryCount > 0 ->
                    supportFragmentManager.popBackStack()
                else -> drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                val frag = AddSubscriptionFragment.newInstance(it)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .addToBackStack(null)
                    .commit()
            }
        }

        parentViewModel.selectedFeed.observe(this, Observer {
            drawerLayout.closeDrawer(GravityCompat.START)
        })
        parentViewModel.title.observe(this, Observer {
            toolbar.title = it
        })
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 ->
                supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }
}