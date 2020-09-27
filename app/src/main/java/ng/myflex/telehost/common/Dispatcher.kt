package ng.myflex.telehost.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class Dispatcher {
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
    open val main: CoroutineContext by lazy { Dispatchers.Main }
}