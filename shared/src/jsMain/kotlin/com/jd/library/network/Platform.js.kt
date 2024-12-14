package com.jd.library.network

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/Js"

    override val baseUrl: String = ""

    override val netBaseUrl: String = ""

    override val intranetBaseUrl: String = ""

    override val appId: String = ""

    override val appKey: String = ""

    override val accessToken: String = ""

    override val isPre: Boolean = true
}

actual fun getPlatform(): Platform = JsPlatform()