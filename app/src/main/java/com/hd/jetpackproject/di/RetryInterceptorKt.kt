package com.hd.jetpackproject.di

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.io.InterruptedIOException


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/12/27
 */
class RetryInterceptorKt internal constructor(builder: Builder) : Interceptor {
    /** 最大重试次数  */
    private var executionCount: Int

    /** 重试的间隔  */
    private val retryInterval: Long

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var retryNum = 0
        while ((!response.isSuccessful) && retryNum <= executionCount) {
            Log.e("RetryInterceptorKt", " " + response.message)
            val nextInterval = retryInterval
            try {
                Thread.sleep(nextInterval)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw InterruptedIOException()
            }
            retryNum++
            response = chain.proceed(request)
        }
        return response
    }

    class Builder {
        var executionCount = 3
        var retryInterval: Long = 500
        fun executionCount(executionCount: Int): Builder {
            this.executionCount = executionCount
            return this
        }

        fun retryInterval(retryInterval: Long): Builder {
            this.retryInterval = retryInterval
            return this
        }

        fun build(): RetryInterceptorKt {
            return RetryInterceptorKt(this)
        }
    }

    init {
        executionCount = builder.executionCount
        retryInterval = builder.retryInterval
    }
}