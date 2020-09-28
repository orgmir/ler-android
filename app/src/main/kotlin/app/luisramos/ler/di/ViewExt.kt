package app.luisramos.ler.di

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import app.luisramos.ler.App
import app.luisramos.ler.ui.ScaffoldViewModel

inline fun <reified VM : ViewModel> View.viewModels(): Lazy<VM> {
    val activity = context as ComponentActivity
    val appContainer = (activity.application as App).appContainer
    return activity.viewModels {
        val parentViewModel = ViewModelLazy(
            ScaffoldViewModel::class,
            { activity.viewModelStore },
            { appContainer.activityViewModelProviderFactory }).value
        appContainer.getViewModelFactory(parentViewModel)
    }
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