package app.luisramos.ler.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.Description
import org.junit.runner.notification.RunListener
import radiography.Radiography
import radiography.ViewStateRenderers.DefaultsIncludingPii

class RadiographyRunListener : RunListener() {
    override fun testRunStarted(description: Description?) {
        val defaultFailureHandler =
            DefaultFailureHandler(InstrumentationRegistry.getInstrumentation().targetContext)
        Espresso.setFailureHandler { error, viewMatcher ->
            try {
                defaultFailureHandler.handle(error, viewMatcher)
            } catch (decoratedError: Throwable) {
                val detailMessageField = Throwable::class.java.getDeclaredField("detailMessage")
                val previouslyAccessible = detailMessageField.isAccessible
                try {
                    detailMessageField.isAccessible = true
                    var message = (detailMessageField[decoratedError] as String?).orEmpty()

                    message = message.substringBefore("\nView Hierarchy:")

                    val hierarchy = Radiography.scan(viewStateRenderers = DefaultsIncludingPii)

                    message += "\nView hierarchies:\n$hierarchy"

                    detailMessageField[decoratedError] = message
                } finally {
                    detailMessageField.isAccessible = previouslyAccessible
                }
                throw decoratedError
            }
        }
    }
}