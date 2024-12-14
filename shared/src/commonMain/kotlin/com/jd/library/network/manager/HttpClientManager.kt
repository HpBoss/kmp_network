package com.jd.library.network.manager

import com.jd.library.network.getPlatform
import com.jd.library.network.interceptor.changeBaseUrlInterceptor
import com.jd.library.network.interceptor.decryptResponse
import com.jd.library.network.interceptor.defaultGatewayVersionInterceptor
import com.jd.library.network.interceptor.gatewayHeaderInterceptor
import com.jd.library.network.interceptor.userAgentInterceptor
import com.jd.library.network.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object HttpClientManager {
    /**
     * 初始化 HttpClient，并安装特定的插件和配置
     */
    private val client = HttpClient {
        // 返回 code 非“2xx”时会报异常
        expectSuccess = true
        // 连接、请求、socket 超时时间设置
        install(HttpTimeout) {
            val timeout = Constants.TIMEOUT
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
        /**
         * 在客户端和服务器之间协商媒体类型。
         * 序列化在发送请求和接收响应时以特定格式反序列化内容。
         */
        install(ContentNegotiation) {
            json(Json {
                // 忽略 Json 中的未知属性
                ignoreUnknownKeys = true
                /**
                 * 指定 Json 实例是否使用 JsonNames 注释。
                 * 当根本不使用 JsonNames 时禁用此标志有时可能会带来更好的性能，
                 * 特别是当使用ignoreUnknownKeys 跳过大量字段时。默认为 true。
                 */
                useAlternativeNames = false
                prettyPrint = true
            })
        }
        // 设置日志打印级别，或者过滤特定日志信息
        install(Logging) {
            level = LogLevel.BODY
        }
        // 请求失败或者发生异常，尝试重连
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
    }.apply {
        // 添加请求拦截器
        plugin(HttpSend).apply {
            intercept { request -> changeBaseUrlInterceptor(request) }
            intercept { request -> defaultGatewayVersionInterceptor(request) }
            intercept { request -> gatewayHeaderInterceptor(request) }
            intercept { request -> userAgentInterceptor(request) }
        }
        // 添加响应拦截器
        responsePipeline.apply {
            intercept(HttpResponsePipeline.Receive) { decryptResponse(this.context.request, it) }
        }
    }

    fun color(): HttpRequest = HttpRequest(GatewayType.COLOR, client)

    fun v1(): HttpRequest = HttpRequest(GatewayType.V1, client)

    fun v2(): HttpRequest = HttpRequest(GatewayType.V2, client)

    suspend fun post(
        action: String,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf()
    ): HttpResponse {
        return HttpRequest(GatewayType.NONE, client).makeRequest(
            action,
            HttpMethod.Post,
            params,
            headers
        )
    }

    suspend fun get(
        action: String,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf()
    ): HttpResponse {
        return HttpRequest(GatewayType.NONE, client).makeRequest(
            action,
            HttpMethod.Get,
            params,
            headers
        )
    }

    fun getURL(action: String): Url {
        val url = Url(getPlatform().netBaseUrl)
        val builder = URLBuilder().apply {
            protocol = url.protocol
            host = url.host
            encodedPath = url.encodedPath
            parameters.append("appid", getPlatform().appId)
            parameters.append("api", action)
        }
        return Url(builder)
    }

}