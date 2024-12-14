package com.jd.library.network

interface Platform {
    // iOS、harmon 需要确认之前的 name
    val name: String

    val baseUrl: String

    val netBaseUrl: String

    val intranetBaseUrl: String

    val appId: String

    val appKey: String

    val accessToken: String

    val isPre: Boolean
}

expect fun getPlatform(): Platform