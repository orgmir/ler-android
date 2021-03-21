package app.luisramos.ler.di

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import app.luisramos.ler.App

inline fun <reified VM : ViewModel> ComponentActivity.appViewModels() = viewModels<VM> {
    val appContainer = (application as App).appContainer
    appContainer.activityViewModelProviderFactory
}

fun <T> LiveData<T>.observe(view: View, observer: Observer<T>) {
    view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View?) {
            this@observe.observeForever(observer)
        }

        override fun onViewDetachedFromWindow(p0: View?) {
            this@observe.removeObserver(observer)
            view.removeOnAttachStateChangeListener(this)
        }
    })
}