package foo.bar.foreapollo3.api

import co.early.fore.kt.core.logging.Logger
import foo.bar.foreapollo3.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * This will be specific to your own app.
 */
class CustomGlobalRequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val requestBuilder = original.newBuilder()

        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader(
            "User-Agent",
            "fore-example-user-agent-" + BuildConfig.VERSION_NAME
        )

        requestBuilder.method(original.method(), original.body())

        return chain.proceed(requestBuilder.build())
    }
}
