package com.jd.library.network

class JsDeviceInfo(
    override val deviceWidth: Int = 0,
    override val deviceHeight: Int = 0,
    override val deviceUniqueId: String = "",
    override val pushDeviceToken: String = "",
    override val appVersionName: String = "",
    override val locale: String = "",
    override val timeZone: String = "",
    override val deviceType: String = "",
    override val osVersion: String = "",
    override val internetType: String = "",
    override val brand: String = "",
    override val model: String = ""
) : DeviceInfo

actual fun getDeviceInfo(): DeviceInfo = JsDeviceInfo()