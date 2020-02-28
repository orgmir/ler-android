package app.luisramos.thecollector

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import app.luisramos.thecollector.ui.main.MainFragment
import app.luisramos.thecollector.ui.subscription.AddSubscriptionFragment
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : AppCompatActivity() {

    private val mainFragment
        get() = supportFragmentManager.findFragmentByTag("main_frag") as MainFragment

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
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 ->
                supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }
}
