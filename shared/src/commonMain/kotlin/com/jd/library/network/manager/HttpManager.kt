package com.jd.library.network.manager

import com.jd.library.network.ColorRequestBuilder
import com.jd.library.network.interceptor.handleRequest
import com.jd.library.network.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmStatic


class HttpManager {
    /**
     * 初始化 HttpClient，并安装特定的插件和配置
     */
    private val httpClient = HttpClient {
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
        // 添加拦截器
        plugin(HttpSend).apply {
            intercept { request -> handleRequest(request) }
        }
    }

    suspend fun post(
        action: String,
        headers: MutableMap<String, String>,
        params: MutableMap<String, Any>
    ) {
        makeRequest(
            httpClient,
            getUrl(action),
            params,
            headers,
            headers[Constants.HEADER_KEY_GATEWAY_VERSION] == "true"
        )
    }

    suspend fun get(
        action: String,
        params: Map<String, String>,
        headers: Map<String, String>
    ) {
        makeRequest(
            httpClient,
            getUrl(action),
            params,
            headers,
            headers[Constants.HEADER_KEY_GATEWAY_VERSION] == "true"
        )
    }

    private suspend fun makeRequest(
        client: HttpClient,
        url: String,
        params: Map<String, Any>,
        headers: Map<String, String>,
        isGateway: Boolean
    ): HttpResponse {
        return client.request {
            this.url(
                if (isGateway && headers[Constants.HEADER_KEY_METHOD_GET] == "true") {
                    appendParamsToUrl(url, params)
                } else url
            )

            headers.forEach { (key, value) ->
                this.header(key, value)
            }

            method = when {
                !isGateway -> HttpMethod.Post
                headers[Constants.HEADER_KEY_METHOD_GET] == "true" -> HttpMethod.Get
                else -> HttpMethod.Post
            }

            if (!isGateway) {
                // 无网关，使用 form-data 提交
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(FormDataContent(Parameters.build {
                    params.forEach { (key, value) -> append(key, value) }
                }))
            } else if (method == HttpMethod.Post) {
                // 有网关的 POST 请求，使用 JSON 提交
                contentType(ContentType.Application.Json)
                setBody(params)
            }
        }
    }

    private fun appendParamsToUrl(baseUrl: String, params: Map<String, String>): String {
        val queryParams = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        return if (baseUrl.contains("?")) {
            "$baseUrl&$queryParams"
        } else {
            "$baseUrl?$queryParams"
        }
    }

    private fun getUrl(action: String): String {
        return ""
    }

    companion object {
        @JvmStatic
        fun color(): ColorRequestBuilder = ColorRequestBuilder()
    }

}