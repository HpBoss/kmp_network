package com.jd.library.network.interceptor

import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder

suspend fun Sender.handleRequest(request: HttpRequestBuilder): HttpClientCall {
    val originalCall = execute(request)
    return if (originalCall.response.status.value !in 100..399) {
        execute(request)
    } else {
        originalCall
    }
}
