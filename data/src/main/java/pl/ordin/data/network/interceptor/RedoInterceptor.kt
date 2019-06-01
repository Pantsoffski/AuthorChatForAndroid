package pl.ordin.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RedoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // Retrieve original request.
        //val original = chain.request()

        return chain.proceed(chain.request())
    }

}