package com.jd.library.network.interceptor

import com.jd.library.network.getDeviceInfo
import com.jd.library.network.getEncrypt
import com.jd.library.network.getPlatform
import com.jd.library.network.getUserInfo
import com.jd.library.network.manager.HttpClientManager
import com.jd.library.network.utils.Constants
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponseContainer
import kotlinx.datetime.Clock
import kotlin.random.Random

/**
 * @Author : 何飘
 * @createTime : 2024/12/14
 */
suspend fun Sender.changeBaseUrlInterceptor(request: HttpRequestBuilder): HttpClientCall {
    val gatewayVersion = request.headers[Constants.HEADER_KEY_GATEWAY_VERSION]
    val action = request.headers[Constants.HEADER_KEY_ACTION] ?: execute(request)
    if (Constants.HEADER_GATEWAY_NONE == gatewayVersion) {
        request.url(getNoneGatewayUrl(request, action.toString()))
    } else if (gatewayVersion == Constants.HEADER_GATEWAY_V1 || gatewayVersion == Constants.HEADER_GATEWAY_V2) {
        request.url(HttpClientManager.getURL(action.toString()))
    }
    // TODO add MockEnable
    return execute(request)
}

suspend fun Sender.defaultGatewayVersionInterceptor(request: HttpRequestBuilder): HttpClientCall {
    val gatewayVersion = request.headers[Constants.HEADER_KEY_GATEWAY_VERSION]
    if (gatewayVersion == null || gatewayVersion == Constants.HEADER_GATEWAY_NONE) {
        request.headers[Constants.HEADER_KEY_GATEWAY_VERSION] = Constants.HEADER_GATEWAY_V1
    }
    return execute(request)
}

suspend fun Sender.gatewayHeaderInterceptor(request: HttpRequestBuilder): HttpClientCall {
    val gatewayVersion = request.headers[Constants.HEADER_KEY_GATEWAY_VERSION]
    if (gatewayVersion == Constants.HEADER_GATEWAY_V2 || gatewayVersion == Constants.HEADER_GATEWAY_V1) {
        return execute(request)
    }
    val deviceId = getDeviceInfo().deviceUniqueId
    val ts = Clock.System.now()
    val nonce = (1..32).joinToString("") { Random.nextInt(0, 16).toString(16) }
    val accessToken = getPlatform().accessToken
    val action = request.url.parameters["api"]
    var xSign = action + "&" + getPlatform().appId + "&" + getPlatform().appKey
    xSign += if (accessToken.isEmpty()) "&$accessToken$ts" else "&$ts"

    request.headers.apply {
        append("x-did", deviceId)
        append("x-client", getPlatform().name)
        append("x-app-version", getDeviceInfo().appVersionName)
        append("x-ts", ts.toString())
        append("x-nonce", nonce)
        append("x-language", getDeviceInfo().locale)
        append("x-os-version", getDeviceInfo().osVersion)
        append("x-network-type", getDeviceInfo().internetType)
        append("x-brand", getDeviceInfo().brand)
        append("x-model", getDeviceInfo().model)
        append("x-imei", deviceId)
        append("x-longitude", getUserInfo().lng)
        append("x-latitude", getUserInfo().lat)
        append("x-tenant-code", getUserInfo().tenantCode)
        append("x-sign", getEncrypt().getMd5(xSign))
        append(
            "x-os-name",
            getPlatform().name
        ) // 为什么这里 Android 项目里写的“android”，上面的“x-client”又写的 Android
        append("x-machine-type", getDeviceInfo().model)
        append("x-channel", getUserInfo().channel)
        append("x-device-type", getDeviceInfo().deviceType)
    }

    if (accessToken.isNotEmpty()) {
        request.headers.append("x-token", accessToken)
    }

    if (getPlatform().isPre) {
        request.headers.append("x-stage", "PRE")
    }

    val deviceToken = getDeviceInfo().pushDeviceToken
    if (deviceToken.isNotEmpty()) {
        request.headers.append("x-device-token", deviceToken)
    }
    return execute(request)
}

suspend fun Sender.userAgentInterceptor(request: HttpRequestBuilder): HttpClientCall {
    request.headers.append("User-Agent","JDME/${getDeviceInfo().appVersionName}")
    return execute(request)
}

suspend fun decryptResponse(request: HttpRequest, container: HttpResponseContainer) {

}

private fun getNoneGatewayUrl(request: HttpRequestBuilder, action: String): String {
    if (action.startsWith("http")) {
        return action
    }
    return if (request.headers[Constants.HEADER_KEY_INTRANET] != null) {
        getPlatform().netBaseUrl + Constants.SLASH + action
    } else {
        getPlatform().netBaseUrl + Constants.SLASH + action
    }
}
