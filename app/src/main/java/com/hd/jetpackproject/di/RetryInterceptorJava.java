package com.hd.jetpackproject.di;


import android.util.Log;

import java.io.IOException;
import java.io.InterruptedIOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptorJava implements Interceptor {
    /**
     * 最大重试次数
     */
    private final int executionCount;
    /**
     * 重试的间隔
     */
    private final long retryInterval;

    RetryInterceptorJava(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int retryNum = 0;
        while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
            if (response == null) {
                Log.e("RetryInterceptorJava", " " + response);
            } else {
                Log.e("RetryInterceptorJava", " " + response.message());
            }
            final long nextInterval = getRetryInterval();
            try {
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            }
            retryNum++;
            response = chain.proceed(request);
        }
        return response;
    }

    /**
     * retry间隔时间
     */
    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {
        private int executionCount;
        private long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = 500;
        }

        public Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public RetryInterceptorJava build() {
            return new RetryInterceptorJava(this);
        }
    }
}
