package ng.myflex.telehost.common

import kotlinx.coroutines.Dispatchers
import ng.myflex.telehost.common.Dispatcher
import kotlin.coroutines.CoroutineContext

class TestDispatcher : Dispatcher() {
    override val IO: CoroutineContext = Dispatchers.Unconfined

    override val main: CoroutineContext = Dispatchers.Unconfined
}