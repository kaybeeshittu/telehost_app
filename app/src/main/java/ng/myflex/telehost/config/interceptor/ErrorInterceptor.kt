package ng.myflex.telehost.config.interceptor

import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.exception.UnAuthroizedException
import ng.myflex.telehost.service.SessionStorageService
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor constructor(
    private val sessionStorageService: SessionStorageService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code() == 401) {
            sessionStorageService.remove(Constant.authSession)

//            throw UnAuthroizedException()
        }
        return response
    }
}