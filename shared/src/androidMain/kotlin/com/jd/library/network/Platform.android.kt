package com.jd.library.network

/**
 * @Author : 何飘
 * @createTime : 2024/12/14
 */
class AndroidPlatform : Platform {
    override val name: String = "Android"

    override val baseUrl: String = GatewayNetEnvironment.getCurrentEnv().baseUrl

    override val netBaseUrl: String = NetEnvironment.getCurrentEnv().baseUrl

    override val intranetBaseUrl: String = NetEnvironment.getCurrentEnv().intranetBaseUrl

    override val appId: String = GatewayNetEnvironment.getCurrentEnv().appId

    override val appKey: String = GatewayNetEnvironment.getCurrentEnv().appKey

    override val accessToken: String = ""

    override val isPre: Boolean = GatewayNetEnvironment.getCurrentEnv().isPre
}

actual fun getPlatform(): Platform = AndroidPlatform()

