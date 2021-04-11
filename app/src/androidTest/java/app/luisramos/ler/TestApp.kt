package app.luisramos.ler

import androidx.test.platform.app.InstrumentationRegistry
import app.luisramos.ler.di.AppContainer

class TestApp : App() {

    companion object {
        val testAppContainer: TestAppContainer
            get() {
                val applicationContext =
                    InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                return (applicationContext as TestApp).appContainer as TestAppContainer
            }
    }

    override fun createAppContainer(): AppContainer = TestAppContainer(this)
}