package app.luisramos.ler.test

import androidx.test.espresso.intent.Intents
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class IntentsTestRule: TestRule {
    override fun apply(base: Statement?, description: Description?): Statement =
        object: Statement() {
            override fun evaluate() {
                Intents.init()
                base?.evaluate()
                Intents.release()
            }
        }
}