package ng.myflex.telehost.config.interceptor

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.service.SessionStorageService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

class JwtInterceptor constructor(
    private val storageService: SessionStorageService,
    private val gson: Gson
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = appendToken(chain)
        val response = chain.proceed(request)

        val body = response.body()
        val path = request.url().encodedPath()
        if (path == "/api/auth/login"
            && response.code() == 200
            && body != null
        ) {
            val content = body.string()
            val token = getToken(content)

            storageService.save(Constant.authSession, token)

            return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), content))
                .build()
        }

        return response
    }

    private fun appendToken(chain: Interceptor.Chain): Request {
        val key = "Authorization"
        val request = chain.request()
        val token = storageService.getString(Constant.authSession)
        if (token != null && request.header(key) == null) {
            val authorization = "Bearer $token"
            return request.newBuilder().addHeader(key, authorization).build()
        }
        return request
    }

    private fun getToken(content: String): String {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val map: Map<String, Any> = gson.fromJson(content, type)

        return map["token"] as String
    }
}