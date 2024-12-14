package com.jd.library.network.manager

import com.jd.library.network.utils.Constants
import io.ktor.client.HttpClient
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

/**
 * @Author: 何飘
 * @Date: 2024/12/14 15:41
 * @Description:
 */
class HttpRequest(private val gatewayType: GatewayType, private val client: HttpClient) {
    private val headers: MutableMap<String, String> = mutableMapOf()
        get() {
            field += when (gatewayType) {
                GatewayType.COLOR -> Constants.HEADER_KEY_GATEWAY_VERSION to Constants.HEADER_GATEWAY_COLOR
                GatewayType.NONE -> Constants.HEADER_KEY_GATEWAY_VERSION to Constants.HEADER_GATEWAY_NONE
                GatewayType.V1 -> Constants.HEADER_KEY_GATEWAY_VERSION to Constants.HEADER_GATEWAY_V1
                GatewayType.V2 -> Constants.HEADER_KEY_GATEWAY_VERSION to Constants.HEADER_GATEWAY_V2
            }
            return field
        }

    private val params: MutableMap<String, Any> = mutableMapOf()

    suspend fun post(
        action: String,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf()
    ): HttpResponse {
        headers[Constants.HEADER_KEY_METHOD_GET] = "false"
        this.headers += headers
        return makeRequest(action, HttpMethod.Post, params, headers)
    }

    suspend fun postDynamic(
        action: String,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf()
    ): HttpResponse {
        return makeRequest(action, HttpMethod.Post, params, headers)
    }

    suspend fun get(
        action: String,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf()
    ): HttpResponse {
        headers[Constants.HEADER_KEY_METHOD_GET] = "true"
        this.headers += headers
        return makeRequest(action, HttpMethod.Post, params, headers)
    }

    internal suspend fun makeRequest(
        action: String,
        method: HttpMethod,
        params: MutableMap<String, Any> = mutableMapOf(),
        headers: MutableMap<String, String> = mutableMapOf(),
    ): HttpResponse {
        return client.request {
            val url = HttpClientManager.getURL(action)
            headers["Content-Type"] = "application/json;charset=UTF-8"
            headers[Constants.HEADER_KEY_ACTION] = action
            if (gatewayType != GatewayType.COLOR) {
                if (headers.isEmpty() && GatewayType.NONE == gatewayType) {
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(FormDataContent(Parameters.build {
                        params.forEach { (key, value) -> append(key, value.toString()) }
                    }))
                } else {
                    if (headers.isEmpty() && method == HttpMethod.Get) {
                        this.url(appendParamsToUrl(url.toString(), params))
                    } else {
                        contentType(ContentType.Application.Json)
                        setBody(params)
                    }
                }
            } else {
                setBody(params)
            }

            if (gatewayType == GatewayType.NONE) {
                this.header(Constants.HEADER_KEY_GATEWAY_VERSION, Constants.HEADER_GATEWAY_V1)
            }

            headers.forEach { (key, value) -> this.header(key, value) }
        }
    }

    private fun appendParamsToUrl(baseUrl: String, params: Map<String, Any>?): String {
        val queryParams = params?.entries?.joinToString("&") { "${it.key}=${it.value}" }
        return if (baseUrl.contains("?")) {
            "$baseUrl&$queryParams"
        } else {
            "$baseUrl?$queryParams"
        }
    }
}