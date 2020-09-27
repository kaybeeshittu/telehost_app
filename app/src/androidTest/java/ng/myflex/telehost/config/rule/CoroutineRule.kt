package ng.myflex.telehost.config.rule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.CoroutineContext

class CoroutineRule : TestRule, CoroutineScope {
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope: TestCoroutineScope = TestCoroutineScope(dispatcher)

    override val coroutineContext: CoroutineContext
        get() = testCoroutineScope.coroutineContext

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)

                base?.evaluate()

                Dispatchers.resetMain()
                testCoroutineScope.cleanupTestCoroutines()
            }
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest {
            runBlocking(coroutineContext) {
                block()
            }
        }
}