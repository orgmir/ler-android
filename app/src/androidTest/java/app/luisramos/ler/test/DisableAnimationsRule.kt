package app.luisramos.ler.test

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DisableAnimationsRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                disableAnimations()
                try {
                    base?.evaluate()
                } finally {
                    enableAnimations()
                }
            }
        }

    private fun disableAnimations() = toggleAnimations(enable = false)

    private fun enableAnimations() = toggleAnimations(enable = true)

    private fun toggleAnimations(enable: Boolean) {
        val scale = if (enable) "1" else "0"
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand("settings put global transition_animation_scale $scale")
            executeShellCommand("settings put global window_animation_scale $scale")
            executeShellCommand("settings put global animator_duration_scale $scale")
        }
    }
}