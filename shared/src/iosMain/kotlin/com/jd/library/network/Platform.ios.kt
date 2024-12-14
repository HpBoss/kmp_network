package com.jd.library.network

import platform.UIKit.UIDevice

class IOSPlatform(
) : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val baseUrl: String = ""

    override val netBaseUrl: String = ""

    override val intranetBaseUrl: String = ""

    override val appId: String = ""

    override val appKey: String = ""

    override val accessToken: String = ""

    override val isPre: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()