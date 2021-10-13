package com.hd.jetpackproject.di

import com.hd.jetpackproject.utils.LOG
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset


/**
 * @author Rocky
 * @description
 * @date 2021/4/22.
 */
class PostInterceptor : Interceptor {
    companion object {
        const val TAG = "PostInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url
        val encodedPath = httpUrl.encodedPath
        LOG.d(TAG, "httpUrl：$httpUrl")
        val body = request.body
        if (body != null) {
            val buffer = Buffer()
            body.writeTo(buffer)
            var charset = Charset.forName("UTF-8")
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
            }
            return chain.proceed(chain.request())
        }

        return chain.proceed(request)
    }
}