package ng.myflex.telehost.util

sealed class ServiceResponse<out T> {
    data class OnSuccess<out T>(val data: T) : ServiceResponse<T>()
    data class OnError<out T>(val error: Throwable) : ServiceResponse<T>()
}

suspend fun <T> execute(block: suspend () -> T): ServiceResponse<T> = try {
    ServiceResponse.OnSuccess(block())
} catch (e: Throwable) {
    ServiceResponse.OnError(e)
}