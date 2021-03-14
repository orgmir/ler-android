package app.luisramos.ler.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar
import tools.fastlane.screengrab.cleanstatusbar.MobileDataType

class CleanStatusBarRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                CleanStatusBar()
                    .setMobileNetworkDataType(MobileDataType.LTE)
                    .enable()
                base?.evaluate()
                CleanStatusBar.disable()
            }
        }
}